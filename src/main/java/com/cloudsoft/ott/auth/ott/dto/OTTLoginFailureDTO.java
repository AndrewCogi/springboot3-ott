package com.cloudsoft.ott.auth.ott.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // objectMapper 역직렬화를 위함
@Getter
public class OTTLoginFailureDTO {
	private String errorMessage;
}
