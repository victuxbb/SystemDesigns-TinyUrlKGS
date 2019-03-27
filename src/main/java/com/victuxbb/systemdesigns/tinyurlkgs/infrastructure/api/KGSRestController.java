package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.api;

import com.victuxbb.systemdesigns.tinyurlkgs.application.KGSUseCase;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class KGSRestController {
    private final KGSUseCase KGSUseCase;

    public KGSRestController(KGSUseCase KGSUseCase) {
        this.KGSUseCase = KGSUseCase;
    }

    @PostMapping("/keys_request")
    public Flux<Key> keysRequest(@RequestBody KeysRequest keysRequest) {
        return Mono.justOrEmpty(keysRequest.getQuantity())
                .flatMapMany(KGSUseCase::getKeys);

    }

}
