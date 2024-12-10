package com.cloudsoft.ott.auth.ott.model;

import java.time.Instant;

import org.springframework.security.authentication.ott.OneTimeToken;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomOneTimeToken implements OneTimeToken {
	private String tokenValue;
	private String userEmail;
	private Instant expiresAt;

	@Override
	public String getTokenValue() {
		return this.tokenValue;
	}

	@Override
	@JsonIgnore // Jackson은 직렬화 시, getter도 모두 직렬화함. 이미 userEmail로 저장되므로 제외시킴
	public String getUsername() {
		return this.userEmail;
	}

	@Override
	public Instant getExpiresAt() {
		return this.expiresAt;
	}
}
