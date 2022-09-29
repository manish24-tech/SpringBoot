package com.practice.userservice.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.userservice.model.User;

/**
 * This wrapper class is implementation of org.springframework.security.core.userdetails.UserDetailsService
 * It is responsible to construct user detail object to give user service detail service for storing in spring security context
 * Basically principal contain basic information like username and password. so, through implementation class we can additional information like email, telephone..etc 
 * @author Manish Luste
 *
 */
public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	/*Additional field - for storing and registering in user detail */
	private Long id;
	private String email;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	/* Default field - that bind with user org.springframework.security.core.userdetails.UserDetails */
	private String username;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	/**
	 * Constructing/preparing user based on role and detail object to be persist 
	 * @param user
	 * @return
	 */
	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getEmail(),
				user.getPassword(), 
				authorities);
	}
	
	/* implementation method of org.springframework.security.core.userdetails.UserDetails */
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	// RETURN collection of granted authority list instance : it means allow authenticate user to grant access for activities to be perform based on role
	// set list of granted authority
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// like this - return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}
}
