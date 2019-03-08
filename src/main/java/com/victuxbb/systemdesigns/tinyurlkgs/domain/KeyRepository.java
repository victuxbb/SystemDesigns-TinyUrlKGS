package com.victuxbb.systemdesigns.tinyurlkgs.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KeyRepository {
    Mono<Void> saveUnusedKey(Flux<Key> keys);
    Flux<Key> getUnusedKeys(long count);
}
