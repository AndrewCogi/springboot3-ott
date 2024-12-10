package com.cloudsoft.ott.auth.ott.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.cloudsoft.ott.auth.ott.model.AuthStatus;
import com.cloudsoft.ott.auth.ott.repository.AuthStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthStatusService {
	private final AuthStatusRepository authStatusRepository;

	public AuthStatus generate(String userEmail, String authKey) {
		// AuthStatus 정보 생성
		Instant expiresAt = Instant.now().plusSeconds(60 * 5); // 5분

		// AuthStatus 생성 및 Redis에 저장
		AuthStatus authStatus = new AuthStatus(userEmail, authKey, false, expiresAt);
		authStatusRepository.saveAuthStatus(authStatus);

		return authStatus;
	}

	public AuthStatus consume(String userEmail) {
		// AuthStatus 조회
		AuthStatus authStatus = authStatusRepository.getAuthStatus(userEmail);

		// Invalid : null 반환
		if (authStatus == null || authStatus.getExpiresAt().isBefore(Instant.now())) {
			return null;
		}

		// Valid : AuthStatus 반환 (+Redis 에서 삭제)
		authStatusRepository.deleteAuthStatus(userEmail);
		return authStatus;
	}

	public AuthStatus setVerify(String userEmail, boolean isVerified) {
		// AuthStatus 조회
		AuthStatus authStatus = authStatusRepository.getAuthStatus(userEmail);

		// Invalid : null 반환
		if (authStatus == null || authStatus.getExpiresAt().isBefore(Instant.now())) {
			return null;
		}

		// Valid : AuthStatus 의 isVerified 업데이트
		authStatus.setVerified(isVerified);
		authStatus.setExpiresAt(Instant.now().plusSeconds(60 * 3)); // 3분
		authStatusRepository.saveAuthStatus(authStatus);
		return authStatus;
	}
}
