package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.bootstrap;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyGenerator;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class KeyLoader {

    private static Logger LOGGER = LoggerFactory.getLogger(KeyLoader.class);
    private final KeyGenerator keyGenerator;
    private final KeyRepository keyRepository;
    private final ReactiveRedisConnectionFactory factory;

    public KeyLoader(KeyGenerator keyGenerator, KeyRepository keyRepository, ReactiveRedisConnectionFactory factory) {
        this.keyGenerator = keyGenerator;
        this.keyRepository = keyRepository;
        this.factory = factory;
    }


    @PostConstruct
    public void loadData() {
        factory.getReactiveConnection()
            .serverCommands()
            .flushAll()
            .flatMapMany(s -> keyGenerator.generateKeys())
            .flatMap(keyRepository::saveUnusedKey)
            .subscribe(it -> LOGGER.info(String.valueOf(it)));
    }
}
