package com.practice.request;

public class EmployeeRequest {

	private String name;

	private String familyName;

	private String email;
	
	private String userName;

	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	@Override
	public String toString() {
		return "EmployeeRequest [name=" + name + ", familyName=" + familyName + ", email=" + email + ", userName="
				+ userName + ", password=" + password + "]";
	}
}
