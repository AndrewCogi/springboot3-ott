package com.cloudsoft.ott.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
	// general success
	SUCCESS(200, "성공적으로 처리되었습니다."),

	// for OTT (21xx, 41xx, 51xx)
	OTT_GENERATION_SUCCESS(2100, "토큰이 정상적으로 생성 및 전송되었습니다."),
	OTT_GENERATION_FAILURE(4100, "토큰이 정상적으로 생성되지 않았습니다."),
	OTT_LOGIN_SUCCESS(2101, "로그인에 성공하였습니다."),
	OTT_LOGIN_FAILURE(4101, "로그인에 실패하였습니다.");

	private final int code;
	private final String message;
}
