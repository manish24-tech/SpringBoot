package com.practice.workspace.controller; 

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

	
	@GetMapping("/info")
	public ResponseEntity<String> authenticatedInfo() {
		return ResponseEntity.ok("Authenticated - Workspace service is initiated! show Public Content.");
	}
	
	@GetMapping("/info-unauth")
	public ResponseEntity<String> unAuthenticatedInfo() {
		return ResponseEntity.ok("UnAuthenticated - Workspace service is initiated! show Public Content.");
	}
}
