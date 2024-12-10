package com.cloudsoft.ott.auth.ott.repository;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.cloudsoft.ott.auth.ott.model.CustomOneTimeToken;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OTTRepository {
	// Redis에 저장될 Key Prefix
	private static final String PREFIX = "OTT:";

	private final RedisTemplate<String, CustomOneTimeToken> redisTemplate;

	// OTT 저장
	public void saveOTT(CustomOneTimeToken ott) {
		String redisKey = generateRedisKey(ott.getTokenValue());
		Duration timeToLive = Duration.between(ott.getExpiresAt(), Instant.now()).abs();
		redisTemplate.opsForValue().set(redisKey, ott, timeToLive);
	}

	// OTT 조회
	// 성공 : CustomOneTimeToken 반환
	// 실패 : null 반환
	public CustomOneTimeToken getOTT(String tokenValue) {
		String redisKey = generateRedisKey(tokenValue);
		Object ott = redisTemplate.opsForValue().get(redisKey);
		return (ott instanceof CustomOneTimeToken) ? (CustomOneTimeToken) ott : null;
	}

	// OTT 삭제
	public void deleteOTT(String tokenValue) {
		String redisKey = generateRedisKey(tokenValue);
		redisTemplate.delete(redisKey);
	}

	// RedisKey 생성 함수
	private String generateRedisKey(String tokenValue) {
		return PREFIX + tokenValue;
	}
}
