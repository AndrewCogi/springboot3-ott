package com.cloudsoft.ott.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cloudsoft.ott.core.dto.ResponseData;
import com.cloudsoft.ott.core.enums.ResponseCode;

// [IMPORTANT!]
// HttpStatus는, http요청에 대한 결과 (2xx: 성공, 4xx,5xx: 실패(비즈니스 로직에 접근조차 못함))
// ResponseData<>의 code는 비즈니스 로직에 대한 결과 (비즈니스 로직에 접근은 성공한 것)(2xxx: 로직 성공, 4xxx,5xxx: 실패, 이유 등)
public class ResponseBuilder<T> {
	// success: 비즈니스 로직 성공
	// failure: 비즈니스 로직 실패

	// Success (responseCode)
	public static <T> ResponseEntity<ResponseData<T>> success(ResponseCode responseCode) {
		return ResponseEntity.ok(ResponseData.of(responseCode));
	}

	// Success (responseCode + data)
	public static <T> ResponseEntity<ResponseData<T>> success(ResponseCode responseCode, T data) {
		return ResponseEntity.ok(ResponseData.of(responseCode, data));
	}

	// Success (responseCode + status)
	public static <T> ResponseEntity<ResponseData<T>> success(ResponseCode responseCode, HttpStatus status) {
		return ResponseEntity.status(status).body(ResponseData.of(responseCode));
	}

	// Success (responseCode + data + status)
	public static <T> ResponseEntity<ResponseData<T>> success(ResponseCode responseCode, T data, HttpStatus status) {
		return ResponseEntity.status(status).body(ResponseData.of(responseCode, data));
	}

	// Failure (responseCode)
	public static <T> ResponseEntity<ResponseData<T>> failure(ResponseCode responseCode) {
		return ResponseEntity.ok(ResponseData.of(responseCode));
	}

	// Failure (responseCode + data)
	public static <T> ResponseEntity<ResponseData<T>> failure(ResponseCode responseCode, T data) {
		return ResponseEntity.ok(ResponseData.of(responseCode, data));
	}

	// Failure (responseCode + status)
	public static <T> ResponseEntity<ResponseData<T>> failure(ResponseCode responseCode, HttpStatus status) {
		return ResponseEntity.status(status).body(ResponseData.of(responseCode));
	}

	// Failure (responseCode + data + status)
	public static <T> ResponseEntity<ResponseData<T>> failure(ResponseCode responseCode, T data, HttpStatus status) {
		return ResponseEntity.status(status).body(ResponseData.of(responseCode, data));
	}
}
