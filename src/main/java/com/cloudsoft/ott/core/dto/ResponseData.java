package com.cloudsoft.ott.core.dto;

import com.cloudsoft.ott.core.enums.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_EMPTY) // 비어있지 않은 값만 JSON 화
@NoArgsConstructor // objectMapper 역직렬화를 위함
@AllArgsConstructor // builder를 위함
@Getter
public class ResponseData<T> {
	private int code; // 비즈니스 상태 코드(Http 상태 코드와 별개)
	private String message;
	private T data;

	public static <T> ResponseData<T> of(ResponseCode responseCode) {
		return ResponseData.<T>builder()
				.code(responseCode.getCode())
				.message(responseCode.getMessage())
				.build();
	}

	public static <T> ResponseData<T> of(ResponseCode responseCode, T data) {
		return ResponseData.<T>builder()
				.code(responseCode.getCode())
				.message(responseCode.getMessage())
				.data(data)
				.build();
	}

}
