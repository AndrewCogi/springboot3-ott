package com.cloudsoft.ott.auth.ott.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.cloudsoft.ott.auth.ott.dto.OTTLoginFailureDTO;
import com.cloudsoft.ott.auth.ott.dto.OTTLoginSuccessDTO;
import com.cloudsoft.ott.core.dto.ResponseData;
import com.cloudsoft.ott.core.enums.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class OTTVerificationController {

	@GetMapping("/auth/ott/login")
	public String handleRedirect(@RequestParam(value = "token", required = false) String token, Model model) {
		// token 파라메터 없을 경우 에러처리
		if(token == null){
			model.addAttribute("errorMessage", "zzzzzzzzzzz");
			return "ott-login/failure";
		}
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://localhost:8080/auth/ott/verify?token=" + token;
		ResponseEntity<String> response;

		// 토큰 검증을 위한 POST 요청
		try {
			response = restTemplate.postForEntity(url, null, String.class);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "xxx");
			return "ott-login/failure";
		}

		try {
			// response.getBody()에서 code 에 대한 값 받아오기
			ObjectMapper objectMapper = new ObjectMapper();
			int responseCode = objectMapper.readTree(response.getBody()).get("code").asInt();

			// 로그인 성공
			if (responseCode == ResponseCode.OTT_LOGIN_SUCCESS.getCode()) {
				// SuccessDTO 로 변환
				ResponseData<OTTLoginSuccessDTO> result = objectMapper.readValue(response.getBody(),
						new TypeReference<ResponseData<OTTLoginSuccessDTO>>() {
						});

				// model에 저장
				model.addAttribute("userEmail", result.getData().getUserEmail());
				model.addAttribute("successMessage", result.getData().getSuccessMessage());
				model.addAttribute("ttl", result.getData().getTtl());

				return "ott-login/success";
			}
			// 로그인 실패
			else {
				// FailureDTO 로 변환
				ResponseData<OTTLoginFailureDTO> result = objectMapper.readValue(response.getBody(),
						new TypeReference<ResponseData<OTTLoginFailureDTO>>() {
						});

				// model에 저장
				model.addAttribute("errorMessage", result.getData().getErrorMessage());

				return "ott-login/failure";
			}
		}
		// code 값이 없을 경우 발생
		catch (JsonProcessingException e) {
			// model에 저장
			model.addAttribute("errorMessage", "JsonProcessingException");

			return "ott-login/failure";
		}
	}
}
