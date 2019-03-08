package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.bootstrap;

import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyGenerator;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

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
        final Long[] times = new Long[2];
        factory.getReactiveConnection()
            .serverCommands()
            .flushAll()
            .flatMapMany(s -> keyGenerator.generateKeys())
            .compose(keyRepository::saveUnusedKey)
            .subscribeOn(Schedulers.parallel())
            .doOnSubscribe(subscription -> times[0] = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            .doOnComplete(() -> times[1] = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
            .subscribe(
                it -> LOGGER.trace("Batch Processed."),
                ex -> LOGGER.error(ex.getLocalizedMessage(), ex),
                () -> LOGGER.info(String.format("Key reset completed in: %d seconds.", (times[1] - times[0])))
            );
    }
}
