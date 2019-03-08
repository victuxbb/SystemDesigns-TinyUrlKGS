package com.victuxbb.systemdesigns.tinyurlkgs.domain;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class KeyGenerator {

    private final Base64Dictionary base64Dictionary;
    private final int length;

    public KeyGenerator(Base64Dictionary base64Dictionary, int length) {
        this.base64Dictionary = base64Dictionary;
        this.length = length;
    }

    public Flux<Key> generateKeys() {
        return Flux.merge(
                Flux.range(0, length)
                        .reduce(Flux.just(""), (keyFlux, i) -> keyFlux.compose(this::addCharacter)))
                .map(Key::new);
    }

    private Flux<String> addCharacter(Flux<String> in) {
        List<Character> dictionary = this.shuffleDictionary();
        return in.flatMap(s ->
                Flux.range(0, dictionary.size())
                        .map(i -> s + dictionary.get(i))
                        .subscribeOn(Schedulers.parallel())
        );
    }

    private List<Character> shuffleDictionary() {
        List<Character> dictionary = Arrays.asList(this.base64Dictionary.getCharacters());
        Collections.shuffle(dictionary);
        return dictionary;
    }
}
