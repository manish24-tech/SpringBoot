package com.practice.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practice.userservice.model.User;
import com.practice.userservice.repository.UserRepository;
/**
 * This wrapper class is implementation of org.springframework.security.core.userdetails.UserDetailsService
 * Because jpa is nothing to do with any service provider(oauth, sso, ldap) to manage user detail.
 * So, we have to deal with user detail service wrapper because we constructing user object from our custom database
 * @author Manish Luste
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
	}
	
	@Transactional
	public UserDetails loadUserByEmail(String email) {
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

		return UserDetailsImpl.build(user);
	}

}
