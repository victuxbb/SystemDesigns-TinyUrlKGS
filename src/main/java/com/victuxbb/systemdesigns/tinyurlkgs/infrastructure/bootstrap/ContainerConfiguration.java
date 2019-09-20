package com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.bootstrap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victuxbb.systemdesigns.tinyurlkgs.application.KGSUseCase;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Base64Dictionary;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.Key;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyGenerator;
import com.victuxbb.systemdesigns.tinyurlkgs.domain.KeyRepository;
import com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.repository.RedisKeyRepository;
import com.victuxbb.systemdesigns.tinyurlkgs.infrastructure.serializer.KeyCombinedSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ContainerConfiguration {

    @Bean
    public Base64Dictionary base64Dictionary() {
        return new Base64Dictionary();
    }

    @Bean
    public KeyGenerator keyGenerator(Base64Dictionary base64Dictionary, @Value("${kgs.keys.length}") int keyLength) {
        return new KeyGenerator(base64Dictionary, keyLength);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer keySerialization() {
      return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
          .serializerByType(Key.class, new KeyCombinedSerializer.KeyJsonSerializer())
          .deserializerByType(Key.class, new KeyCombinedSerializer.KeyJsonDeserializer());
    }

    @Bean
    public ReactiveRedisOperations<String, Key> redisOperations(
            ReactiveRedisConnectionFactory factory,
            ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Key> serializer = new Jackson2JsonRedisSerializer<>(Key.class);
        serializer.setObjectMapper(objectMapper);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Key> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Key> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public KeyRepository keyRepository(ReactiveRedisOperations<String, Key> redisOperations) {
        return new RedisKeyRepository(redisOperations.opsForSet());
    }


    @Bean
    public KGSUseCase resetKGSUseCase(KeyRepository keyRepository) {
        return new KGSUseCase(keyRepository);
    }

}
