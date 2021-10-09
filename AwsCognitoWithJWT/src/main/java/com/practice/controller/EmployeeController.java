package com.practice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.CodeDeliveryDetailsType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.practice.modal.Employee;
import com.practice.request.EmployeeRequest;
import com.practice.service.EmployeeService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping(value = "/api/v1")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/* Authenticated API - Need JWT token in request headers*/
	
	@GetMapping(value = "/employee")
	public Employee getEmployeeById(@RequestParam(value = "employeeId") Long employeeId) {
		return employeeService.getEmployeeById(employeeId);
	}

	@GetMapping(value = "/employee/{email}")
	public Employee getEmployeeByEmail(@PathVariable(value = "email") String email) {
		return employeeService.getEmployeeByEmail(email);
	}
	
	
	/* Un authenticated API - No Need JWT token in request headers */
	
	@PostMapping(value = "/aws/user")
	public SignUpResult signUpUser(@Validated @RequestBody EmployeeRequest employeeRequest) {
		return employeeService.signUpUser(employeeRequest);
	}
	
	@PostMapping(value = "/aws/user/confirmation/{username}/{confirmationCode}")
	public ConfirmSignUpResult confirmUser(@PathVariable(value = "username") String username, @PathVariable(value = "confirmationCode") String confirmationCode) {
		return employeeService.confirmUser(username, confirmationCode);
	}
	
	@GetMapping(value = "/aws/user/{username}")
	public AdminGetUserResult getUser(@PathVariable(value = "username") String username) {
		return employeeService.getUser(username);
	}
	
	@PutMapping(value = "/aws/user/{username}")
	public AdminUpdateUserAttributesResult updateUser(@PathVariable(value = "username") String username, @RequestParam(value = "name") String name, @RequestParam(value = "familyName") String familyName) {
		return employeeService.updateUser(username, name, familyName);
	}

	@DeleteMapping(value = "/aws/user")
	public AdminDeleteUserResult deleteUser(@RequestParam(value = "username") String username) {
		return employeeService.deleteUser(username);
	}
	
	@PostMapping(value = "/aws/user/confirmation/{username}")
	public CodeDeliveryDetailsType sendConfirmationCodeToUser(String username) {
		return employeeService.sendConfirmationCodeToUser(username);
	} 

}
