package com.cloudsoft.ott.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.cloudsoft.ott.auth.ott.model.AuthStatus;
import com.cloudsoft.ott.auth.ott.model.CustomOneTimeToken;
import com.cloudsoft.ott.core.properties.RedisProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@RequiredArgsConstructor
public class RedisConfig {
	private final RedisProperties redisProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		// Redis 설정
		RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
		redisConfiguration.setHostName(redisProperties.getHost());
		redisConfiguration.setPort(redisProperties.getPort());
		redisConfiguration.setPassword(redisProperties.getPassword());
		// 설정 반환
		return new LettuceConnectionFactory(redisConfiguration);
	}

	// for AuthStatus template
	@Bean
	public RedisTemplate<String, AuthStatus> authKeyRedisTemplate() {
		RedisTemplate<String, AuthStatus> redisTemplate = new RedisTemplate<>();

		// Redis 연결
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		// Key는 String 직렬화
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance,
				ObjectMapper.DefaultTyping.NON_FINAL);

		// Value는 GenericJackson2JsonRedisSerializer로 설정
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(serializer);

		return redisTemplate;
	}

	// for CustomOneTimeToken template
	@Bean
	public RedisTemplate<String, CustomOneTimeToken> customOneTimeTOkenRedisTemplate() {
		RedisTemplate<String, CustomOneTimeToken> redisTemplate = new RedisTemplate<>();

		// Redis 연결
		redisTemplate.setConnectionFactory(redisConnectionFactory());

		// Key는 String 직렬화
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		// ObjectMapper 설정
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.activateDefaultTyping(
				LaissezFaireSubTypeValidator.instance,
				ObjectMapper.DefaultTyping.NON_FINAL);

		// Value는 GenericJackson2JsonRedisSerializer로 설정
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(serializer);

		return redisTemplate;
	}
}
