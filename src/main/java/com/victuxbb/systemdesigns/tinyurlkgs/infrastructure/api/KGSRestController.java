package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.api;

import com.victuxbb.systemdesigns.tinyurlkgs.application.KGSUseCase;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class KGSRestController {
    private final KGSUseCase KGSUseCase;

    public KGSRestController(KGSUseCase KGSUseCase) {
        this.KGSUseCase = KGSUseCase;
    }

    @GetMapping("/keys")
    public Flux<Key> getKey() {
        return KGSUseCase.getKeys();
    }
}
