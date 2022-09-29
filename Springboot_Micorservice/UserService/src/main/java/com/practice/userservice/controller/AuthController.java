package com.practice.userservice.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.practice.userservice.dto.jwt.JwtResourceDTO;
import com.practice.userservice.dto.jwt.UserCredentialRequest;
import com.practice.userservice.dto.requestpayload.PasswordRecorvery;
import com.practice.userservice.dto.requestpayload.SignInRequest;
import com.practice.userservice.dto.requestpayload.SignupRequest;
import com.practice.userservice.dto.responsepayload.LoggedInUserResponse;
import com.practice.userservice.dto.responsepayload.MessageResponse;
import com.practice.userservice.model.Role;
import com.practice.userservice.model.User;
import com.practice.userservice.model.UserRole;
import com.practice.userservice.repository.RoleRepository;
import com.practice.userservice.repository.UserRepository;
import com.practice.userservice.service.UserDetailsImpl;
import com.practice.userservice.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/v1/users/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	@LoadBalanced
	RestTemplate restTemplate;

	// bind user detail service to get user detail from database via user name
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
		
	//@Autowired
	//EmailSenderService emailSenderService;
	
	@GetMapping("/info")
	public ResponseEntity<String> info() {
		return ResponseEntity.ok("UnAuthenticated - AuthController service is initiated! show Public Content.");
	}
	
	@PostMapping("/signin")
	public ResponseEntity<LoggedInUserResponse> authenticateUser(@Valid @RequestBody SignInRequest loginRequest) {

		// validate user and password
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		HttpEntity<UserCredentialRequest> requestBody = new HttpEntity<UserCredentialRequest>(new UserCredentialRequest(loginRequest.getUsername(), loginRequest.getPassword()));
		
		ResponseEntity<JwtResourceDTO> postForEntity = restTemplate.postForEntity(
				"http://API-GATEWAY/gateway/auth/token",
			    requestBody,
		        JwtResourceDTO.class);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new LoggedInUserResponse(postForEntity.getBody().getAccessToken(), 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<MessageResponse> registerUser(HttpServletRequest request, @Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(UserRole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		
		addUpdatedUserDetailInSecurityContext(request, signUpRequest.getUsername());

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	
	
	@PutMapping("/forgot-password/{email}")
	public ResponseEntity<?> forgotUserPassword(HttpServletRequest request, @PathVariable("email") String email) {
		
		Optional<Boolean> userOptional = Optional
				.ofNullable(userRepository.existsByEmail(email));
		
		if (!userOptional.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email does not exists for password recovery!"));
		}
		
		User userObj = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
		String newPassword = "Test@123"; //emailSenderService.getSystemGeneratedPassword();
		
		// update existing user's account
		User user = new User(userObj.getId(),userObj.getUsername(), 
				userObj.getEmail(), encoder.encode(newPassword), userObj.getRoles());
				
		userRepository.save(user);
		
		addUpdatedUserDetailInSecurityContext(request, user.getUsername());
		
		// create the email
	/*	SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setSubject("Password Recovery Request!");
		mailMessage.setFrom("abc@gmail.com");
		
		StringJoiner userDetailTemplate = new StringJoiner("");
		userDetailTemplate.add("\n\n Email : "+user.getEmail());
		userDetailTemplate.add("\n\n User Name : "+user.getUsername());
		userDetailTemplate.add("\n\n Role : "+ user.getRoles().stream().map(val -> val.getName()).collect(Collectors.toList()));
		userDetailTemplate.add("\n\n New Password : "+newPassword);
		
		mailMessage.setText("Hi "+user.getUsername()+", \n You requested for new password based on forgot password policy that you login with detail as : \n "+userDetailTemplate.toString());
		
		emailSenderService.sendEmail(mailMessage);*/
		return ResponseEntity.ok(new MessageResponse("Password recovery email is sent to your register email id with new password!"));	
	}
	
	@PutMapping("/reset-password")
	public ResponseEntity<?> resetUserPassword(HttpServletRequest request, @Valid @RequestBody PasswordRecorvery passwordInfo) {
		
		Optional<Boolean> userOptional = Optional
				.ofNullable(userRepository.existsByEmail(passwordInfo.getEmail()));
		
		if (!userOptional.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email does not exists for password recovery!"));
		}
		
		User userObj = userRepository.findByEmail(passwordInfo.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + passwordInfo.getEmail()));
		
		Optional<Boolean> isPasswordMatch = Optional
			.ofNullable(new BCryptPasswordEncoder().matches(passwordInfo.getOldPassword(), userObj.getPassword()));
		
		if (!isPasswordMatch.isPresent()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Old password does not match during recovery!"));
		} 
			
		userObj = new User(userObj.getId(),userObj.getUsername(), 
				userObj.getEmail(), encoder.encode(passwordInfo.getNewPassword()), userObj.getRoles());
		userRepository.save(userObj);
		
		addUpdatedUserDetailInSecurityContext(request, userObj.getUsername());
		 
		return ResponseEntity.ok(new MessageResponse("Password reset successfully and email is sent to your register email id with updated password!"));
		
	}
	
	void addUpdatedUserDetailInSecurityContext(HttpServletRequest request, String userName) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
