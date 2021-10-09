package com.practice.service;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesRequest;
import com.amazonaws.services.cognitoidp.model.AdminUpdateUserAttributesResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.CodeDeliveryDetailsType;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeRequest;
import com.amazonaws.services.cognitoidp.model.ResendConfirmationCodeResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.practice.configuration.AwsPropertyConfiguration;
import com.practice.request.EmployeeRequest;

/**
 * Service that integrate with Amazon Cognito Identity Provider and responsible to perform user management tasks
 *  
 * @author manish.luste
 */
@Service
public class CognitoClientService {
	
	private static final Log logger = LogFactory.getLog(CognitoClientService.class);
	
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String FAMILY_NAME = "family_name";
	
	private AWSCognitoIdentityProvider client;

	@Autowired
	private AwsPropertyConfiguration awsPropertyConfiguration;
	
	/**
	 * Responsible to setup CognitoClientService bean and initialize setup after application context is ready
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		logger.info("CognitoClient.runAfterStartup() - construct instantce with AWS credential!");
		logger.debug("clientId : " + awsPropertyConfiguration.getClientId() + " | userPool : " + awsPropertyConfiguration.getUserPoolId() + " | ACCESS_KEY : " + awsPropertyConfiguration.getAccessKey()
				+ " | SECRET_KEY :" + awsPropertyConfiguration.getSecretKey());
		logger.debug("CognitoClient.runAfterStartup() : Constructing AWSCognitoIdentityProvider Instance ");
		client = createCognitoClient();
		logger.debug("CognitoClient.runAfterStartup() : AWSCognitoIdentityProvider Instance has been constructed ");
	}

	/**
	 * Responsible to setup Amazon Cognito Identity Provider instance to interact with AWS api/methods.
	 * @return com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
	 */
	private AWSCognitoIdentityProvider createCognitoClient() {
		AWSCredentials cred = new BasicAWSCredentials(awsPropertyConfiguration.getAccessKey(), awsPropertyConfiguration.getSecretKey());
		AWSCredentialsProvider credProvider = new AWSStaticCredentialsProvider(cred);
		return AWSCognitoIdentityProviderClientBuilder.standard().withCredentials(credProvider)
				.withRegion(Regions.US_EAST_2).build();
	}

	/**
	 * Responsible to setup/register user into aws cognito
	 * @param employee employee data to be set up
	 * @return com.amazonaws.services.cognitoidp.model.SignUpResult
	 */
	public SignUpResult signUp(EmployeeRequest employee) {
		logger.info("Signup() request with therapist details ::" + employee.toString());
		SignUpRequest request = new SignUpRequest().withClientId(awsPropertyConfiguration.getClientId()).withUsername(employee.getUserName())
				.withPassword(employee.getPassword()).withUserAttributes(
						new AttributeType().withName(EMAIL).withValue(employee.getEmail()),
						new AttributeType().withName(NAME).withValue(employee.getName()),
						new AttributeType().withName(FAMILY_NAME).withValue(employee.getFamilyName()));
		SignUpResult result = client.signUp(request);
		logger.info("Signup request to cognito completed");
		return result;
	}

	/**
	 * Responsible to confirm already registered user via verification code - that receive over mail
	 * @param email user's email address
	 * @param confirmationCode 4 digit code received over user's email address
	 * @return com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult
	 */
	public ConfirmSignUpResult confirmSignUp(String username, String confirmationCode) {
		ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest().withClientId(awsPropertyConfiguration.getClientId())
				.withUsername(username).withConfirmationCode(confirmationCode);
		return client.confirmSignUp(confirmSignUpRequest);
	}
	
	public void getTokens() {
		//https://ptmantra-test.auth.us-east-2.amazoncognito.com/oauth2/token
	}
	
	/**
	 * Responsible to get AWS cognito user detail by username(emailId).
	 * 
	 * @param username email id of an user.
	 * @return response from the server from the request to get the specified user
	 *         as an administrator.
	 */
	public AdminGetUserResult getUserInfo(String username) {
		logger.info("CognitoClient.getUserInfo()  :: Instantiated");

		AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest().withUsername(username)
				.withUserPoolId(awsPropertyConfiguration.getUserPoolId());

		AdminGetUserResult adminGetUser = client.adminGetUser(adminGetUserRequest);
		logger.info("CognitoClient.getUserInfo()  :: Accomplished");
		return adminGetUser;
	}

	/**
	 * Responsible to update user first name and last name as an AWS cognito user
	 * attributes.
	 * 
	 * @param username  email id of an user. ex. abc@xyz.com.
	 * @param firstName updated first name.
	 * @param lastName  updated last name.
	 * @return the response from the server for the request to update user
	 *         attributes as an administrator.
	 */
	public AdminUpdateUserAttributesResult updateUserAttributes(String username, String name, String familyName) {
		logger.info("CognitoClient.updateUserAttributes()  :: Instantiated");

		List<AttributeType> userAttributesList = Arrays.asList(
				new AttributeType().withName(NAME).withValue(name),
				new AttributeType().withName(FAMILY_NAME).withValue(familyName));

		AdminUpdateUserAttributesRequest adminUpdateUserAttributesRequest = new AdminUpdateUserAttributesRequest()
				.withUserAttributes(userAttributesList).withUsername(username).withUserPoolId(awsPropertyConfiguration.getUserPoolId());
		AdminUpdateUserAttributesResult adminUpdateUserAttributes = client
				.adminUpdateUserAttributes(adminUpdateUserAttributesRequest);
		logger.info("CognitoClient.updateUserAttributes()  :: Accomplished");
		return adminUpdateUserAttributes;
	}

	/**
	 * Responsible to delete an user from AWS cognito as admin
	 * 
	 * @param username email id of an user.
	 * @return com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult
	 */
	public AdminDeleteUserResult deleteUser(String username) {
		logger.info("CognitoClient.deleteUser()  :: Instantiated");
		AdminDeleteUserRequest adminDeleteUserRequest = new AdminDeleteUserRequest().withUsername(username)
				.withUserPoolId(awsPropertyConfiguration.getUserPoolId());
		AdminDeleteUserResult adminDeleteUser = client.adminDeleteUser(adminDeleteUserRequest);
		logger.info("CognitoClient.deleteUser()  :: Accomplished");
		return adminDeleteUser;
	}

	/**
	 * Responsible to re-send confirmation code on user name 
	 * @param username email address of the user
	 * @return com.amazonaws.services.cognitoidp.model.CodeDeliveryDetailsType
	 */
	public CodeDeliveryDetailsType resendConfirmationCode(String username) {
		logger.info("resendConfirmationCode initiated");

		ResendConfirmationCodeRequest resendCode = new ResendConfirmationCodeRequest().withUsername(username)
				.withClientId(awsPropertyConfiguration.getClientId());

		ResendConfirmationCodeResult resendResult = client.resendConfirmationCode(resendCode);
		logger.info("resendConfirmationCode sent");
		return resendResult.getCodeDeliveryDetails();
	}
}
