package com.cloudsoft.ott.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class RedisProperties {
	private String host;
	private int port;
	private String password;
}
