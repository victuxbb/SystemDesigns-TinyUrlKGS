package com.victuxbb.systemdesigns.tinyurlkgs.application;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class KGSUseCase {

    private final KeyRepository keyRepository;

    public KGSUseCase(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public Flux<Key> getKeys(Long quantity) {
        return keyRepository.getUnusedKeys(quantity);

    }
}
