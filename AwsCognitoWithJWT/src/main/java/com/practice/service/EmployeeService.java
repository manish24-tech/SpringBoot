package com.practice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.CodeDeliveryDetailsType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.practice.modal.Employee;
import com.practice.repository.EmployeeRepository;
import com.practice.request.EmployeeRequest;

@Service
public class EmployeeService {

	private EmployeeRepository employeeRepository;
	private CognitoClientService cognitoClientService;
	EmployeeService(EmployeeRepository employeeRepository, CognitoClientService cognitoClientService) {
		this.employeeRepository = employeeRepository;
		this.cognitoClientService = cognitoClientService;
	}
	
	public Employee getEmployeeById(Long empId) {
		Optional<Employee> isEmployee = employeeRepository.findById(empId);
		return isEmployee.isPresent() ? isEmployee.get() : new Employee();
	}

	public Employee getEmployeeByEmail(String email) {
		Optional<Employee> isEmployee = employeeRepository.findByEmail(email);
		return isEmployee.isPresent() ? isEmployee.get() : new Employee();
	}
	
	// 1. sign up user with detail into AWS cognito pool
	public SignUpResult signUpUser(EmployeeRequest employeeRequest) {
		return cognitoClientService.signUp(employeeRequest);
	}
	
	// 2. confirm the user with verification code into AWS cognito pool
	public ConfirmSignUpResult confirmUser(String username, String confirmationCode) {
		return cognitoClientService.confirmSignUp(username, confirmationCode);
	}
	
	// 3. get user information from AWS cognito pool
	public AdminGetUserResult getUser(String username) {
		return cognitoClientService.getUserInfo(username);
	}
	
	// 4. update user information into AWS cognito pool
	public AdminUpdateUserAttributesResult updateUser(String username, String name, String familyName) {
		return cognitoClientService.updateUserAttributes(username, name, familyName);
	}

	// 5. delete user from AWS cognito pool
	public AdminDeleteUserResult deleteUser(String username) {
		return cognitoClientService.deleteUser(username);
	}
	
	// 5. resent confirmation code for registered user - AWS cognito sent an email
	public CodeDeliveryDetailsType sendConfirmationCodeToUser(String username) {
		return cognitoClientService.resendConfirmationCode(username);
	}
}
