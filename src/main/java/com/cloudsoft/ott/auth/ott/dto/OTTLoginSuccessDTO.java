package com.cloudsoft.ott.auth.ott.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // objectMapper 역직렬화를 위함
@Getter
public class OTTLoginSuccessDTO {
	private String userEmail; // 로그인 성공한 사용자
	private String successMessage; // 사용자에게 보여줄 메세지
	private int ttl; // 로그인 가능한 남은 시간(분)
	//private String accessToken;
	//private String refreshToken;
}
