package com.cloudsoft.ott.auth.ott.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 사용자의 앱과 일대일 대응되는 userEmail 를 Key로 하여 redis에 저장
// 이후 authKey 를 통해 OTT 인증 사용자가 앱에서 Jwt 발급할 때 이 값들로 검증된 사용자인지 확인

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthStatus {
	private String userEmail;
	private String authKey;
	private boolean isVerified;
	private Instant expiresAt;
}
