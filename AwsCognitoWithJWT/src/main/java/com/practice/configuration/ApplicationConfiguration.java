package com.practice.configuration;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.practice.aws.AwsCognitoIdTokenProcessor;
import com.practice.aws.AwsCognitoJwtAuthFilter;



/**
 * Responsible to manage and load beans in spring application context
 * Reference of PtMantraApplicationConfiguration has been imported in com.ptmantra.registration.PtMantraRegistrationApplication
 * @author manish.luste
 *
 */
@Configuration 
public class ApplicationConfiguration {
	
	/**
	 * Responsible to process and verify JWT token. Also configure AWS cognito keys URL as JWK(Json Web Keys) source https://cognito-idp.<user-pool-id>.amazonaws.com/<user-pool-region>/.well-known/jwks.json.
	 * JWK(JSON Web Keys) source is nothing but validate and verify token with RS256 algorithm
	 * @return com.nimbusds.jwt.proc.ConfigurableJWTProcessor<SecurityContext>
	 * @throws MalformedURLException Thrown to indicate that a malformed URL has occurred. Either no legal protocol could be found in a specification string or the string could not be parsed.
	 */
	@Bean
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor(
			@Autowired AwsPropertyConfiguration awsPropertyConfiguration) throws MalformedURLException {
		ResourceRetriever resourceRetriever = new DefaultResourceRetriever(
				awsPropertyConfiguration.getConnectionTimeout(), awsPropertyConfiguration.getReadTimeout());
		URL jwkSetURL = new URL(awsPropertyConfiguration.getJwkUrl());
		JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkSetURL, resourceRetriever);
		ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
		JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(RS256, keySource);
		jwtProcessor.setJWSKeySelector(keySelector);
		return jwtProcessor;
	}

	/**
	 * Responsible to validate and verify AWS JWT token
	 * @param awsPropertyConfiguration com.ptmantra.registration.PtMantraAwsPropertyConfiguration
	 * @param configurableJWTProcessor com.nimbusds.jwt.proc.ConfigurableJWTProcessor<SecurityContext>
	 * @return com.practice.aws.AwsCognitoIdTokenProcessor
	 */
	@Bean
	public AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor(
			@Autowired AwsPropertyConfiguration awsPropertyConfiguration,
			@Autowired ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor) {
		return new AwsCognitoIdTokenProcessor(awsPropertyConfiguration, configurableJWTProcessor);
	}

	/**
	 * Responsible to filter each authenticated and unAuthenticated requests and validate with JWT token 
	 * @param awsCognitoIdTokenProcessor
	 * @return com.practice.aws.AwsCognitoJwtAuthFilter
	 */
	@Bean
	public AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter(
			@Autowired AwsCognitoIdTokenProcessor awsCognitoIdTokenProcessor) {
		return new AwsCognitoJwtAuthFilter(awsCognitoIdTokenProcessor);
	}
	
}
