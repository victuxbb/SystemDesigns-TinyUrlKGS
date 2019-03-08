package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RedisKeyRepository implements KeyRepository {

  private static final String UNUSED_TINYURLKEYS = "unusedurlkeys";
  private static final String USED_TINYURLKEYS = "usedurlkeys";

  private final ReactiveSetOperations<String, Key> setOperations;

  public RedisKeyRepository(ReactiveSetOperations<String, Key> setOperations) {
    this.setOperations = setOperations;
  }

  @Override
  public Mono<Long> saveUnusedKey(Key key) {
    return setOperations.add(UNUSED_TINYURLKEYS, key);
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
