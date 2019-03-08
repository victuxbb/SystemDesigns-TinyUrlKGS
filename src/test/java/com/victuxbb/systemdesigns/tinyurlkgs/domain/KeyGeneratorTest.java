package com.victuxbb.systemdesigns.tinyurlkgs.domain;


import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class KeyGeneratorTest {

    private KeyGenerator keyGenerator;

    @Before
    public void setUp() {
        keyGenerator = new KeyGenerator(new Base64Dictionary(), 3);
    }


    @Test
    public void testGenerationV0() {
        assertThat(keyGenerator.generateKeys().collectList().block()).hasSize(262144);
    }
}