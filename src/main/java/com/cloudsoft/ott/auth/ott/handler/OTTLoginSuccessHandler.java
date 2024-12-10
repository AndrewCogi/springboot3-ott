package com.cloudsoft.ott.auth.ott.handler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cloudsoft.ott.auth.ott.dto.OTTLoginSuccessDTO;
import com.cloudsoft.ott.auth.ott.model.AuthStatus;
import com.cloudsoft.ott.auth.ott.service.AuthStatusService;
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
public class OTTLoginSuccessHandler implements AuthenticationSuccessHandler {
	private final AuthStatusService authStatusService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("[OTTLoginSuccessHandler]");

		// 사용자 정보 추출
		String userEmail = authentication.getName();

		// AuthStatus 변경 (isVerified -> true, TTL -> 3 min)
		AuthStatus authStatus = authStatusService.setVerify(userEmail, true);

		// 응답 객체 생성
		OTTLoginSuccessDTO ottLoginSuccessDTO = new OTTLoginSuccessDTO(userEmail,
				"인증이 완료되었습니다. 앱으로 돌아가 <b>[인증 완료]</b> 버튼을 눌러주세요.",
				(int) (Duration.between(Instant.now(), authStatus.getExpiresAt()).abs().toMinutes()));
		ResponseEntity<ResponseData<OTTLoginSuccessDTO>> responseEntity = ResponseBuilder
				.success(ResponseCode.OTT_LOGIN_SUCCESS, ottLoginSuccessDTO);

		// 응답 작성
		response.setStatus(responseEntity.getStatusCode().value()); // HTTP 상태 설정
		response.setContentType("application/json"); // JSON 응답 설정
		response.setCharacterEncoding("UTF-8"); // 응답 인코딩 설정
		response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
		// 이후 실행되는 DefaultResourcesFilter에서 정적 리소스를 찾는데, restful 서버이기 때문에
		// 정적 리소스를 제공하지 않는다. 따라서 DefaultResourcesFilter를 제거하려했으나, 제거가 어려워
		// getWriter().flush()를 사용하여 이후 필터 처리를 막는다.
		// [주의] 사용하지 않으면 404 not found error가 발생!
		response.getWriter().flush();
	}
}
