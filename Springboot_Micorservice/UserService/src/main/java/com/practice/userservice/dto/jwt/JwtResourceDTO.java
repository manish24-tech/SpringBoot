package com.practice.userservice.dto.jwt;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(content = Include.NON_NULL)
public class JwtResourceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	public JwtResourceDTO() {}
	
	public JwtResourceDTO(String userName, String accessToken) {
		this.username = userName;
		this.accessToken = accessToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@Override
	public String toString() {
		return "JwtResourceDTO [username=" + username + ", accessToken=" + accessToken + "]";
	}
	
}

