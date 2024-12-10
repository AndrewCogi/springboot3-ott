package com.cloudsoft.ott.auth.ott.handler;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cloudsoft.ott.auth.ott.dto.OTTGenerationDTO;
import com.cloudsoft.ott.auth.ott.service.AuthStatusService;
import com.cloudsoft.ott.core.dto.ResponseData;
import com.cloudsoft.ott.core.enums.ResponseCode;
import com.cloudsoft.ott.core.util.EmailSender;
import com.cloudsoft.ott.core.util.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OTTGenerationSuccessHandler implements OneTimeTokenGenerationSuccessHandler {
	private final EmailSender emailSender;
	private final AuthStatusService authStatusService;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
			throws IOException, ServletException {
		// 사용자 이메일 및 OTT 추출
		String userEmail = oneTimeToken.getUsername();
		String tokenValue = oneTimeToken.getTokenValue();

		// 로그인 시도한 앱과 일대일 대응할 UUID 생성
		String authKey = UUID.randomUUID().toString();

		// AuthStatus 객체 Redis에 저장
		authStatusService.generate(userEmail, authKey);

		// 이메일 전송
		String url = "http://192.168.35.91:8080/auth/ott/login?token=" + tokenValue;
		emailSender.sendVerificationEmail(userEmail, url);

		System.out.println(
				"Email Message : Click this link for verification [http://localhost:8080/auth/ott/login?token="
						+ tokenValue + "]");

		// 응답 객체 생성
		OTTGenerationDTO ottGenerationDTO = new OTTGenerationDTO(userEmail, authKey);
		ResponseEntity<ResponseData<OTTGenerationDTO>> responseEntity = ResponseBuilder
				.success(ResponseCode.OTT_GENERATION_SUCCESS, ottGenerationDTO);

		// 응답 작성
		response.setStatus(responseEntity.getStatusCode().value()); // HTTP 상태 설정
		response.setContentType("application/json"); // JSON 응답 설정
		response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody())); // body 작성
	}

}
