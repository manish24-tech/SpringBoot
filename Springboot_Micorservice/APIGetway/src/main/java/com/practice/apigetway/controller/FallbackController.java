package com.practice.apigetway.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.apigetway.exception.ErrorResponseDTO;

@RestController
@RequestMapping(path = { "/fallback" } , produces = APPLICATION_JSON_VALUE)
public class FallbackController {

	@GetMapping(path = "/service/{name}")
	public ResponseEntity<ErrorResponseDTO> serviceFallback(@PathVariable(name = "name") String serviceName) {
		
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.SERVICE_UNAVAILABLE.value(),
				HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
				serviceName.concat(" is no yet active. please contact to system administrator!"));
		
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
	}
}