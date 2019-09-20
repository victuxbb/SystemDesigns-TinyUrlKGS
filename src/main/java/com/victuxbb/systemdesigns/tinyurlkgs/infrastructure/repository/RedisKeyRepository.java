package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveSetOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class RedisKeyRepository implements KeyRepository {

  private static Logger LOGGER = LoggerFactory.getLogger(RedisKeyRepository.class);
  private static final String UNUSED_TINYURLKEYS = "unusedurlkeys";
  private static final String USED_TINYURLKEYS = "usedurlkeys";

  private final ReactiveSetOperations<String, Key> setOperations;

  public RedisKeyRepository(ReactiveSetOperations<String, Key> setOperations) {
    this.setOperations = setOperations;
  }

  @Override
  public Mono<Void> saveUnusedKey(Flux<Key> keys) {
    AtomicLong counter = new AtomicLong();
    return keys.buffer(100)
        .flatMap(keyList ->
            setOperations.add(UNUSED_TINYURLKEYS, keyList.toArray(new Key[0]))
            .subscribeOn(Schedulers.elastic())
        )
        .publishOn(Schedulers.parallel())
        .filter(aLong -> aLong > 0)
        .switchIfEmpty(Mono.error(new RedisKeyRepositoryException()))
        .buffer(10000)
        .map(longs -> longs.stream().mapToLong(Long::longValue).sum())
        .doOnNext(processedElements -> LOGGER.info("Processed {} elements", counter.addAndGet(processedElements)))
        .then();
  }

  @Override
  public Flux<Key> getUnusedKeys(long count) {
    return setOperations.pop(UNUSED_TINYURLKEYS, count)
        .flatMap(key -> setOperations.add(USED_TINYURLKEYS, key)
            .filter(aLong -> aLong == 1)
            .map(aLong -> key)
        );
  }
}
