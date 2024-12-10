package com.cloudsoft.ott.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.cloudsoft.ott.auth.ott.handler.OTTGenerationSuccessHandler;
import com.cloudsoft.ott.auth.ott.handler.OTTLoginFailureHandler;
import com.cloudsoft.ott.auth.ott.handler.OTTLoginSuccessHandler;
import com.cloudsoft.ott.auth.ott.provider.OTTAuthenticationProvider;
import com.cloudsoft.ott.auth.ott.service.OTTService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
	private final OTTService ottService;
	private final OTTGenerationSuccessHandler ottGenerationSuccessHandler;
	private final OTTAuthenticationProvider ottAuthenticationProvider;
	private final OTTLoginSuccessHandler ottLoginSuccessHandler;
	private final OTTLoginFailureHandler ottLoginFailureHandler;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// [CSRF 설정]
				// non-browser client 를 사용할 것이기에 비활성화 (Spring Security 공식 문서 참고)
				.csrf((csrf) -> csrf.disable())

				// [Session 설정]
				// 세션 비활성화 (세션 사용 x, 생성된 세션이 있어도 사용 x)
				.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// [기존 로그인 및 인증 설정]
				// HTTP 기존 인증 및 로그인 페이지 비활성화 (Jwt 사용 예정)
				.httpBasic((httpBasic) -> httpBasic.disable())
				.formLogin((formLogin) -> formLogin.disable())

				// [인증/비인증 경로 설정]
				.authorizeHttpRequests((requests) -> {
					// resources
					requests.requestMatchers("/ott-login/**", "/favicon.ico").permitAll();
					// controller
					requests.requestMatchers("/auth/ott/login", "/error").permitAll();
					// authenticated
					requests.anyRequest().authenticated();
				})

				// [OneTimeToken 설정]
				// [OTT 전체 흐름]
				// tokenGeneratingUrl 로 토큰 생성 요청
				// tokenService 가 토큰 생성 및 저장
				// tokenGenerationSuccessHandler 가 OTT 를 사용자 Email 로 전송
				// 사용자가 OTTVerificationController 로 검증 요청(GET)
				// OTTVerificationController 가 loginProcessingUrl 로 검증 요청(POST)
				// authenticationProvider 가 tokenService를 사용하여 검증(조회 및 조회 성공 시 삭제) 및
				// Authentication 객체 생성
				// 검증 성공 시, authenticationSuccessHandler가 이후 로직 처리
				// 검증 실패 시, authenticationFailureHandler 이후 로직 처리
				.oneTimeTokenLogin((ott) -> {
					// OTT 입력 페이지 비활성화 (DefaultOneTimeTokenSubmitPageGeneratingFilter 비활성화)
					ott.showDefaultSubmitPage(false);
					// OTT 생성 요청 경로
					// 생성 Url : http://localhost:8080/auth/ott/generate?username=[userEmail]
					ott.tokenGeneratingUrl("/auth/ott/generate");
					// OTT 검증 요청 경로
					// 검증 Url : http://localhost:8080/auth/ott/verify?token=[tokenValue]
					ott.loginProcessingUrl("/auth/ott/verify");
					// OTT 생성 및 조회 담당 Service 설정
					// 역할 : OTT 생성 및 Redis 에 저장 / OTT 조회 및 검증, Redis에서 삭제
					ott.tokenService(ottService);
					// OTT 생성 성공 이후 로직을 담당할 Handler 설정
					// 역할 : OTT 를 인자로 받아 Email 작성 및 전송
					ott.tokenGenerationSuccessHandler(ottGenerationSuccessHandler);
					// OTT 검증을 담당. 이후 결과에 따라 SuccessHandler, FailureHandler로 넘김
					ott.authenticationProvider(ottAuthenticationProvider);
					// OTT 인증 성공 이후 로직을 담당할 Handler 설정
					// 역할 : Jwt 발행 및 Redis 에 저장
					ott.authenticationSuccessHandler(ottLoginSuccessHandler);
					// OTT 인증 실패 이후 로직을 담당할 Handler 설정
					// 역할 : 인증 실패 이유 반환
					ott.authenticationFailureHandler(ottLoginFailureHandler);
				});

		// [Filter 설정]

		return httpSecurity.build();
	}
}
