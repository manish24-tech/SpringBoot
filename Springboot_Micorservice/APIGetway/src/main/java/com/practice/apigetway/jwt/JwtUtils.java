package com.practice.apigetway.jwt;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.practice.apigetway.configuration.JWTConfiguration;
import com.practice.apigetway.exception.AuthenticationException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Responsible to get user detail(principal) by generating and validating jwt
 * token with username, date, expiration, secret
 * 
 * @author Manish Luste
 *
 */
@Component
public class JwtUtils {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	private final JWTConfiguration jwtConfiguration;

	public JwtUtils(JWTConfiguration jwtConfiguration) {

		this.jwtConfiguration = jwtConfiguration;
	}

	private static final String BEARER = "Bearer ";
	private static final String INVALID_JWT_SIGNATURE = "Invalid JWT signature";
	private static final String INVALID_JWT_TOKEN = "Invalid JWT token";
	private static final String JWT_TOKEN_EXPIRED = "JWT token is expired";
	private static final String UNSUPPORTED_JWT_TOKEN = "JWT token is unsupported";
	
	/**
	 * Responsible to building jwt token with user principle- user name and secrete
	 * key that going to exchange in header with hashing algorithm with expiration
	 * time in millisecond
	 * 
	 * @param username user name from principal object after successful
	 *                 authentication
	 * @return fresh jwt token that will store at client-side in cookie
	 */
	public String generateJwtToken(String userName) {

		// building jwt token with user principle, secrete key that going to exchange in
		// header with JWA hashing algorithm {@code HMAC using SHA-512} name for with
		// expiration time in millisecond
		return Jwts.builder().setSubject(userName).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtConfiguration.getExpirationMs()))
				.signWith(SignatureAlgorithm.HS512, jwtConfiguration.getSecret()).compact();
	}

	/**
	 * Responsible to get user name form jwt token body with matching secrete key
	 * which exchange in header during intercepting request
	 * 
	 * @param authToken token that should same as generated token
	 * @return user name from token body via getSubject()
	 */
	public String getUserNameFromJwtToken(String authToken) {
		try {
			Jws<Claims> validateJwtToken = validateJwtToken(authToken);
			return validateJwtToken.getBody().getSubject();
		} catch (AuthenticationException e) {
			logger.error("Something went wrong to JWS claim: {}", e);
		}
		
		return StringUtils.EMPTY;
	}

	public Jws<Claims> validateJwtToken(String authToken) throws AuthenticationException{
		try {
			return Jwts.parser().setSigningKey(jwtConfiguration.getSecret()).parseClaimsJws(authToken);
		} catch (SignatureException e) {
			logger.error("JWS signature validation fails: {}",e);
			throw new AuthenticationException(INVALID_JWT_SIGNATURE);
		} catch (MalformedJwtException e) {
			logger.error("Claim is not a valid JWS: {}",e);
			throw new AuthenticationException(INVALID_JWT_TOKEN);
		} catch (ExpiredJwtException e) {
			logger.error("JWS Claims has been expired: {}",e);
			throw new AuthenticationException(JWT_TOKEN_EXPIRED);
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token does not represent an Claims JWS: {}", e);
			throw new AuthenticationException(UNSUPPORTED_JWT_TOKEN);
		} catch (IllegalArgumentException e) {
			logger.error("JWS Claim is null or empty or only whitespace: {}", e);
			throw new AuthenticationException(INVALID_JWT_SIGNATURE);
		} 
	}

	/**
	 * Responsible to get jwt token from header(Authorization) and removed Bearer
	 * from the start index
	 * 
	 * @param request ServerHttpRequest object that intercepted
	 * @return customized header with removed Bearer : From Authorization :
	 *         Barere_jwttoken To Authorization : jwttoken
	 */
	public String getTokenFromHeader(ServerHttpRequest serverHttpRequest) {

		if (null != serverHttpRequest.getHeaders()
				&& null != serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION)) {
			var headerAuth = serverHttpRequest.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			if (headerAuth.startsWith(BEARER)) {
				return headerAuth.substring(7, headerAuth.length());
			}
		}
		return StringUtils.EMPTY;
	}
	
	/**
	 * Responsible to validate the string token weather is blank or not.
	 * @param authToken authToken token that should same as generated token
	 * @return boolean status true/false
	 */
	public boolean isAuthTokenEmpty(String authToken) {
		return StringUtils.isBlank(authToken);
	}	
	
	
	
	

}
