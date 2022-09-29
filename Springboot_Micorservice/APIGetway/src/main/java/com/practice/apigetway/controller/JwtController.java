package com.practice.apigetway.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.apigetway.DTO.JwtResourceDTO;
import com.practice.apigetway.DTO.JwtTokenRequest;
import com.practice.apigetway.DTO.UserCredentialRequest;
import com.practice.apigetway.jwt.JwtUtils;

@RestController
@RequestMapping(path = { "/gateway/auth" }, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@Validated
public class JwtController {

	private final JwtUtils jwtUtils;
	
	public JwtController(JwtUtils jwtUtils) {
		this.jwtUtils = jwtUtils;
	}
	
	@PostMapping(path = "/token")
	public ResponseEntity<?> generateJwtToken(@Valid @RequestBody UserCredentialRequest user) {
		final String token = jwtUtils.generateJwtToken(user.getUserName());
		
		if (StringUtils.isBlank(token)) {
			return new ResponseEntity<>("Unable to generate token as username seems invalid.", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(new JwtResourceDTO(user.getUserName(), token));
	}
	
	@PostMapping(path = "/user")
	public ResponseEntity<?> getUserDetail(@Valid @RequestBody JwtTokenRequest jwtTokenRequest) {
		final String userName = jwtUtils.getUserNameFromJwtToken(jwtTokenRequest.getAccessToken());
		
		if (StringUtils.isBlank(userName)) {
			return new ResponseEntity<>("Unable to generate username from provided token which seems invalid.", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok(new JwtResourceDTO(userName, jwtTokenRequest.getAccessToken()));
	}
}
