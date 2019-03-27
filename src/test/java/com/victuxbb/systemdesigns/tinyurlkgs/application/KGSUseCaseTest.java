package com.victuxbb.systemdesigns.tinyurlkgs.application;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

public class KGSUseCaseTest {

    @Mock
    private KeyRepository keyRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void itShouldGetKeysFromRepository() {

        Long givenKeys = 2L;

        when(keyRepository.getUnusedKeys(givenKeys))
                .thenReturn(Flux.range(0, givenKeys.intValue())
                        .map(String::valueOf)
                        .map(Key::new));


        KGSUseCase kgsUseCase = new KGSUseCase(keyRepository);

        StepVerifier.create(
                kgsUseCase.getKeys(givenKeys)
        )
                .expectNextCount(givenKeys)
                .verifyComplete();

    }
}