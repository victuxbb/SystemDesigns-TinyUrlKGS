package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.BlockingDeque;

public class RedisKeyRepository implements KeyRepository {

  private static final String UNUSED_TINYURLKEYS = "unusedurlkeys";
  private static final String USED_TINYURLKEYS = "usedurlkeys";

  private final ReactiveSetOperations<String, Key> setOperations;

  public RedisKeyRepository(ReactiveSetOperations<String, Key> setOperations) {
    this.setOperations = setOperations;
  }

  @Override
  public Mono<Void> saveUnusedKey(Flux<Key> keys) {
    return keys.buffer(100).flatMap(keyList -> setOperations.add(UNUSED_TINYURLKEYS, keyList.toArray(new Key[0])))
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
