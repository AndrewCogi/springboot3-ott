package com.cloudsoft.ott.auth.ott.handler;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.cloudsoft.ott.auth.ott.dto.OTTLoginFailureDTO;
import com.cloudsoft.ott.core.dto.ResponseData;
import com.cloudsoft.ott.core.enums.ResponseCode;
import com.cloudsoft.ott.core.util.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OTTLoginFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		System.out.println("[OTTLoginFailureHandler]");

		// 응답 객체 생성
		OTTLoginFailureDTO ottLoginFailureDTO = new OTTLoginFailureDTO(exception.getMessage());
		ResponseEntity<ResponseData<OTTLoginFailureDTO>> responseEntity = ResponseBuilder
				.failure(ResponseCode.OTT_LOGIN_FAILURE, ottLoginFailureDTO);

		// 응답 작성
		response.setStatus(responseEntity.getStatusCode().value()); // http 상태 코드 설정
		response.setContentType("application/json"); // JSON 응답 설정
		response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody())); // body 작성
	}
}
