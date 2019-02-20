package com.victuxbb.systemdesigns.tinyurlkgs.domain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class KeyGenerator {

  private final Character[] dictionary;
  private final int lenght = 6;

  public KeyGenerator(Character[] dictionary) {
    assert (dictionary != null);
    assert (dictionary.length > 0);
    this.dictionary = dictionary;
  }
//
//  public Key generateRandomKey() {
//    return new Key(
//        new Random()
//            .ints(6, 0, dictionary.length - 1)
//            .boxed()
//            .map(randomIndex -> dictionary[randomIndex])
//            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
//            .toString()
//    );
//  }

//  public Key generateSequentialKey() {
//    String s = "";
//    for (int d1 = 0; d1 < dictionary.length; d1++) {
//      for (int d2 = 0; d2 < dictionary.length; d2++) {
//        for (int d3 = 0; d3 < dictionary.length; d3++) {
//          for (int d4 = 0; d4 < dictionary.length; d4++) {
//            for (int d5 = 0; d5 < dictionary.length; d5++) {
//              for (int d6 = 0; d6 < dictionary.length; d6++) {
//                s = new StringBuilder()
//                    .append(dictionary[d1])
//                    .append(dictionary[d2])
//                    .append(dictionary[d3])
//                    .append(dictionary[d4])
//                    .append(dictionary[d5])
//                    .append(dictionary[d6])
//                    .toString();
//              }
//            }
//          }
//        }
//      }
//    }
//    System.out.println(s);
//    return new Key(s);
//  }


  public static void main(String[] args) {
    KeyGenerator that = new KeyGenerator(KeyDictionary.base64Alphabet);
    long start1 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
//    List<String> keys = Flux.<String>create(fluxSink -> {
//      that.addCharacter("", 5, fluxSink);
//      fluxSink.complete();
//    })
//        .collectList()
//        .block();
    long end1 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    //System.out.println("option 1, size: " + keys.size() + ", time wasted: " + (end1 - start1));

    long start2 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    List<String> keys2 = Flux.just("")
        .compose(that::addCharacter)
        .compose(that::addCharacter)
        .compose(that::addCharacter)
        .compose(that::addCharacter)
        .compose(that::addCharacter)
        .collectList()
        .block();
    long end2 = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    System.out.println("option 2, size: " + keys2.size() + ", time wasted: " + (end2 - start2));
  }

  public void addCharacter(String in, int depth, FluxSink<String> keySink) {
    if (depth == 0) {
      keySink.next(in);
      return;
    }
    for (int i = 0; i < dictionary.length; i++) {
      addCharacter(in + dictionary[i], depth - 1, keySink);
    }
  }

  public Flux<String> addCharacter(Flux<String> in) {
    return in.flatMap(s ->
        Flux.range(0, dictionary.length)
            .map(i -> s + dictionary[i])
        .subscribeOn(Schedulers.parallel())
    );
  }
}
