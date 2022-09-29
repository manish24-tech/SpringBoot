package com.practice.apigetway.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class UserCredentialRequest {
	
	@JsonProperty(value = "username", required = true)
	@NotEmpty
	@NotBlank
	private String userName;
	
	@JsonProperty(value = "password", required = true)
	@NotEmpty
	@NotBlank
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	
	

}
