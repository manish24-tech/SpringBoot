package com.practice.userservice.dto.responsepayload;

/**
 * Responsible to get error message response like : { "message": "User
 * registered successfully!" }
 * 
 * @author Manish Luste
 *
 */
public class MessageResponse {
	private String message;

	public MessageResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
