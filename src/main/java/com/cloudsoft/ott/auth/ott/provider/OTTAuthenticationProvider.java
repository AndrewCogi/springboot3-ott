package com.cloudsoft.ott.auth.ott.provider;

import java.util.Collections;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.cloudsoft.ott.auth.ott.model.CustomOneTimeToken;
import com.cloudsoft.ott.auth.ott.service.OTTService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OTTAuthenticationProvider implements AuthenticationProvider {
	private final OTTService ottService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// OTT 조회
		OneTimeTokenAuthenticationToken otpAuthenticationToken = (OneTimeTokenAuthenticationToken) authentication;
		CustomOneTimeToken consumed = (CustomOneTimeToken) ottService.consume(otpAuthenticationToken);

		// OTT 검증
		if (consumed == null) {
			throw new InvalidOneTimeTokenException("유효하지 않은 토큰입니다.");
		}

		// 검증 성공 후, Authentication 객체 생성 및 반환
		OneTimeTokenAuthenticationToken authenticated = OneTimeTokenAuthenticationToken
				.authenticated(consumed.getUserEmail(), Collections.emptyList());
		authenticated.setDetails(otpAuthenticationToken.getDetails());
		return authenticated;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OneTimeTokenAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
