package com.victuxbb.systemdesigns.tinyurlkgs.application;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import reactor.core.publisher.Flux;

public class KGSUseCase {

    private final KeyRepository keyRepository;
    private static final int KEYS_BUFFER = 100;


    public KGSUseCase(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    public Flux<Key> getKeys() {
        return keyRepository.getUnusedKeys(KEYS_BUFFER);
    }

}
