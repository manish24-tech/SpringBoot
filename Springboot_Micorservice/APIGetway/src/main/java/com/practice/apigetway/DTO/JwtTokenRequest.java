package com.practice.apigetway.DTO;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class JwtTokenRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "access_token", required = true)
	@NotBlank
	@NotEmpty
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
