package com.practice.aws;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.practice.configuration.AwsPropertyConfiguration;
import com.practice.exception.CongitoJWTException;

/**
 * Responsible to validate and verify AWS JWT token 
 * @author manish.luste
 */
public class AwsCognitoIdTokenProcessor {

	private AwsPropertyConfiguration awsPropertyConfiguration;
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;

	public AwsCognitoIdTokenProcessor(AwsPropertyConfiguration awsPropertyConfiguration,
			ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor) {
		this.awsPropertyConfiguration = awsPropertyConfiguration;
		this.configurableJWTProcessor = configurableJWTProcessor;
	}

	public Authentication authenticate(HttpServletRequest request) throws CongitoJWTException {

		String idToken = request.getHeader(this.awsPropertyConfiguration.getHttpHeader());
		if (idToken != null) {
			JWTClaimsSet claims = null;

			try {
				claims = this.configurableJWTProcessor.process(idToken, null);
			} catch (ParseException | BadJOSEException | JOSEException e1) {
				throw new CongitoJWTException("Invalid Request Token processed with request!", e1,
						HttpStatus.UNAUTHORIZED.value());
			}

			if (claims != null) {
				validateIssuer(claims);
				String username = getUserNameFrom(claims);
				if (username != null) {
					List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
					User user = new User(username, "", new ArrayList<>());
					return new AwsCognitoJwtAuthentication(user, claims, grantedAuthorities);
				}
			}
		}
		return null;
	}

	private String getUserNameFrom(JWTClaimsSet claims) {
		return claims.getClaims().get(this.awsPropertyConfiguration.getUserNameField()).toString();
	}

	private void validateIssuer(JWTClaimsSet claims) throws CongitoJWTException {
		if (!claims.getIssuer().equals(this.awsPropertyConfiguration.getPoolUrl())) {
			throw new CongitoJWTException(String.format("Issuer %s does not match cognito idp %s", claims.getIssuer(),
					this.awsPropertyConfiguration.getPoolUrl()), HttpStatus.FORBIDDEN.value());
		}
	}
}
