package com.github.alfredobaptista.infrastructure.config;

import com.github.alfredobaptista.infrastructure.model.PaymentModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, PaymentModel> redisTemplate(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper // ← injeta o ObjectMapper do Spring Boot
    ) {
        // Reutiliza o ObjectMapper já configurado — JavaTimeModule já registrado
        Jackson2JsonRedisSerializer<PaymentModel> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, PaymentModel.class);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        RedisTemplate<String, PaymentModel> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Chaves e valores
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(serializer);

        // Hash — evita serialização JDK padrão se usar opsForHash()
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet(); // ← garante inicialização correta
        return template;
    }
}