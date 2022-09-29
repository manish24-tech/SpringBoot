package com.practice.apigetway.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JWTConfiguration {

	private String secret;

	private Long expirationMs;

	private Boolean authDisabled;

	private List<String> unauthPath;
	
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpirationMs() {
		return expirationMs;
	}

	public void setExpirationMs(Long expirationMs) {
		this.expirationMs = expirationMs;
	}

	public Boolean getAuthDisabled() {
		return authDisabled;
	}

	public void setAuthDisabled(Boolean authDisabled) {
		this.authDisabled = authDisabled;
	}

	
	public List<String> getUnauthPath() {
		return unauthPath;
	}

	public void setUnauthPath(List<String> unauthPath) {
		this.unauthPath = unauthPath;
	}

	/* Custom methods */
	public boolean isClientRequestSecured(ServerHttpRequest serverHttpRequest) {
		return getUnauthPath().stream().noneMatch(str -> serverHttpRequest.getURI().getPath().contains(str));
	}
	
	
	



}
