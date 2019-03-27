package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.LinkedBlockingQueue;

@Repository
public class InmemoryKeyRepository implements KeyRepository {

  private LinkedBlockingQueue<Key> unusedStore = new LinkedBlockingQueue<>();

  @Override
  public Mono<Void> saveUnusedKey(Flux<Key> keys) {
    return keys
        .doOnNext(key -> unusedStore.offer(key))
        .then();
  }

  @Override
  public Flux<Key> getUnusedKeys(long count) {
    return Flux.from(s -> s.onNext(unusedStore.poll()));
  }
}
