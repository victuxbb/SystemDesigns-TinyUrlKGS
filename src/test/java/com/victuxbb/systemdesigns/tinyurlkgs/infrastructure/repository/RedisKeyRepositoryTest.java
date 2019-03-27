package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ReactiveSetOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RedisKeyRepositoryTest {

    @Mock
    ReactiveSetOperations<String, Key> setOperations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itShouldSaveAnUnusedKey() {


        when(setOperations.add(anyString(), any()))
                .thenReturn(Mono.just(1L));

        RedisKeyRepository redisKeyRepository = new RedisKeyRepository(setOperations);

        Flux<Key> givenKeys = Flux.just(new Key("miau"), new Key("guau"));

        StepVerifier.create(
                redisKeyRepository.saveUnusedKey(givenKeys)
        )
                .then(() -> verify(setOperations, times(1)).add(anyString(), any()))
                .verifyComplete();

    }


    @Test
    public void itShouldReturnErrorWhenDatabaseFail() {


        when(setOperations.add(anyString(), any()))
                .thenReturn(Mono.just(0L));

        RedisKeyRepository redisKeyRepository = new RedisKeyRepository(setOperations);

        Flux<Key> givenKeys = Flux.just(new Key("miau"), new Key("guau"));

        StepVerifier.create(
                redisKeyRepository.saveUnusedKey(givenKeys)
        )
                .then(() -> verify(setOperations, times(1)).add(anyString(), any()))
                .verifyError(RedisKeyRepositoryException.class);

    }




    @Test
    public void itShouldGetUnusedKeys() {

        long givenNumberOfKeysRequested = 2L;

        when(setOperations.pop(any(), eq(givenNumberOfKeysRequested)))
                .thenReturn(Flux.just(new Key("miau"), new Key("guau")));

        when(setOperations.add(anyString(), any()))
                .thenReturn(Mono.just(1L));


        RedisKeyRepository redisKeyRepository = new RedisKeyRepository(setOperations);



        StepVerifier.create(
                redisKeyRepository.getUnusedKeys(givenNumberOfKeysRequested)
        )
                .expectNextCount(2)
                .then(() -> verify(setOperations, times(1)).pop(anyString(), eq(givenNumberOfKeysRequested)))
                .then(() -> verify(setOperations, times(2)).add(anyString(), any()))
                .verifyComplete();
    }


    @Test
    public void itShouldCompleteWithoutUnusedKeys() {

        long givenNumberOfKeysRequested = 2L;

        when(setOperations.pop(any(), eq(givenNumberOfKeysRequested)))
                .thenReturn(Flux.just(new Key("miau"), new Key("guau")));

        when(setOperations.add(anyString(), any()))
                .thenReturn(Mono.just(0L));


        RedisKeyRepository redisKeyRepository = new RedisKeyRepository(setOperations);



        StepVerifier.create(
                redisKeyRepository.getUnusedKeys(givenNumberOfKeysRequested)
        )
                .expectNextCount(0)
                .then(() -> verify(setOperations, times(1)).pop(anyString(), eq(givenNumberOfKeysRequested)))
                .then(() -> verify(setOperations, times(2)).add(anyString(), any()))
                .verifyComplete();
    }

}