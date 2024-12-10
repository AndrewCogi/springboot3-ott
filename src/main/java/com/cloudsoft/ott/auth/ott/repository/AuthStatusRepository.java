package com.cloudsoft.ott.auth.ott.repository;

import java.time.Duration;
import java.time.Instant;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.cloudsoft.ott.auth.ott.model.AuthStatus;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuthStatusRepository {
	// Redis에 저장될 Key Prefix
	private static final String PREFIX = "AUTHSTATUS:";

	private final RedisTemplate<String, AuthStatus> redisTemplate;

	// AuthStatus 저장
	public void saveAuthStatus(AuthStatus authStatus) {
		String redisKey = generateRedisKey(authStatus.getUserEmail());
		Duration timeToLive = Duration.between(authStatus.getExpiresAt(), Instant.now()).abs();
		redisTemplate.opsForValue().set(redisKey, authStatus, timeToLive);
	}

	// AuthStatus 조회
	// 성공 : AuthStatus 반환
	// 실패 : null 반환
	public AuthStatus getAuthStatus(String userEmail) {
		String redisKey = generateRedisKey(userEmail);
		Object authStatus = redisTemplate.opsForValue().get(redisKey);
		return (authStatus instanceof AuthStatus) ? (AuthStatus) authStatus : null;
	}

	// AuthStatus 삭제
	public void deleteAuthStatus(String userEmail) {
		String redisKey = generateRedisKey(userEmail);
		redisTemplate.delete(redisKey);
	}

	// RedisKey 생성 함수
	private String generateRedisKey(String userEmail) {
		return PREFIX + userEmail;
	}
}
