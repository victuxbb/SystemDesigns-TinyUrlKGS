package com.victuxbb.systemdesigns.tinyurlkgs.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface KeyRepository {
    Mono<Long> saveUnusedKey(Key key);
    Flux<Key> getUnusedKeys(long count);
}
