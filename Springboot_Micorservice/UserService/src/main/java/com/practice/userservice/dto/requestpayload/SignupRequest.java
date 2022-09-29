package com.practice.userservice.dto.requestpayload;

import java.util.Set;

import javax.validation.constraints.*;

/**
 * Responsible to create request body for register user like : { "username":
 * "admin", "email": "admin@practice.com", "password": "Test@123", "role":
 * ["mod", "user", "admin"] }
 * 
 * @author Manish Luste
 *
 */
public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}
}
