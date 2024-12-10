package com.cloudsoft.ott.auth.ott.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OTTGenerationDTO {
	private String userEmail; // 토큰 생성 요청 사용자
	private String authKey; // 앱과 일대일 대응되는 UUID 값
}
