package com.victuxbb.systemdesigns.tinyurlkgs.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class KeyGenerator {

  private static Logger LOGGER = LoggerFactory.getLogger(KeyGenerator.class);
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
        .map(Key::new)
        .doOnSubscribe(subscription -> LOGGER.info(
            "Starting the generation of {} keys",
            BigDecimal.valueOf(Math.pow(64, length)).toBigInteger()
        ))
        .subscribeOn(Schedulers.parallel());
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
