package com.cloudsoft.ott.auth.ott.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.stereotype.Service;

import com.cloudsoft.ott.auth.ott.model.CustomOneTimeToken;
import com.cloudsoft.ott.auth.ott.repository.OTTRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OTTService implements OneTimeTokenService {
	private final OTTRepository ottRepository;

	@Override
	public OneTimeToken generate(GenerateOneTimeTokenRequest request) {
		// Token 정보 생성
		String userEmail = request.getUsername();
		String tokenValue = UUID.randomUUID().toString();
		Instant expiresAt = Instant.now().plusSeconds(60 * 5); // 5분

		// OTT 생성 및 Redis에 저장
		CustomOneTimeToken ott = new CustomOneTimeToken(tokenValue, userEmail, expiresAt);
		ottRepository.saveOTT(ott);

		return ott;
	}

	@Override
	public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
		// Token 정보 추출
		String tokenValue = authenticationToken.getTokenValue();

		// Token 조회
		CustomOneTimeToken ott = ottRepository.getOTT(tokenValue);

		// Invalid : null 반환 (기존 구현 방법을 따름)
		if (ott == null || ott.getExpiresAt().isBefore(Instant.now())) {
			return null;
		}

		// Valid : Token 반환 (+Redis 에서 삭제)
		ottRepository.deleteOTT(tokenValue);
		return ott;
	}
}
