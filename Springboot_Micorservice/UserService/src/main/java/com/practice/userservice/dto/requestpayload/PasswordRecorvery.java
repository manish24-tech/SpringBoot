package com.practice.userservice.dto.requestpayload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * Responsible to create request body forgot and reset password :
 * 
 * @author Manish Luste
 *
 */

public class PasswordRecorvery {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String oldPassword;
	
	@NotBlank
	private String newPassword;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "PasswordRecorvery [email=" + email + ", oldPassword=" + oldPassword + ", newPassword=" + newPassword
				+ "]";
	}

}
