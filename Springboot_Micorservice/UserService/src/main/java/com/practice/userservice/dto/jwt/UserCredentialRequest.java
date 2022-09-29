package com.practice.userservice.dto.jwt;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data 
public class UserCredentialRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "username", required = true)
	@NotEmpty
	@NotBlank
	private String userName;
	
	@JsonProperty(value = "password", required = true)
	@NotEmpty
	@NotBlank
	private String password;

	public UserCredentialRequest(@NotEmpty @NotBlank String userName,
			@NotEmpty @NotBlank String password) {
		this.userName = userName;
		this.password = password;
	}

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
