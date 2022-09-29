package com.practice.userservice.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import com.practice.userservice.components.AuthEntryPointJwt;

import com.practice.userservice.service.UserDetailsServiceImpl;


/**   
 * EnableWebSecurity : tell spring to apply global security on classes 
 * EnableGlobalMethodSecurity : provide @PreAuthorize, @PostAuthorize AOP security on methods
 * 
 * @author Manish Luste
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig  {
	
	// bind user detail service wrapper to perform authentication and authorization - username and password
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	// bind AuthEntryPoint that enable ExceptionTranslationFilter to commence an authentication scheme to thrown 401 in case of un authenticate user tries to access HTTP secure resource
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	/**
	 * Authentication process with building user credential that retrieve user detail
	 * Authentication manager instruct authentication builder to build user detail with encoded password 
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
	  
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (webSecurity) -> webSecurity.ignoring().antMatchers("/h2-console/**", "/resources/**", "/static/**",
				"/css/**", "/js/**", "/images/**");
	}

	/**
	 * Authorization Process of authenticated user on each type of filtered request from filter chain
	 * HttpSecurity configurations to configure cors, csrf, session management, rules for protected resources
	 */
	 @Bean
	  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
			// CONFIGURE CROSS-ORIGIN RESOURCE SHARING (CORS) 
			httpSecurity.cors().configurationSource(corsConfigurationSource());

			// DISABLED CROSS-SITE REQUEST FORGERY (CSRF)
			httpSecurity.csrf().disable();

			// NOT ALLOWED TO STORE RESPONSE AS CACHE (NO-CACHE, NO-STORE, MAX-AGE=0, MUST-REVALIDATE) 
			httpSecurity.headers().cacheControl();
			

			// CONFIGURE ERROR HANDLER TO SEND UNAUTHORIZED (401) RESPONSE
			httpSecurity.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

			
			// ENABLE H2-CONSOLE IF ANY
			.headers().frameOptions().sameOrigin()

			// REST END POINTS ARE STATELESS (MEANS NO SESSION BEING CREATED) 
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			// ALLOW ALL HEALTH END-POINTS
			.and().authorizeRequests().antMatchers("/actuator/**").permitAll()

			// ALLOW ALL PREFLIGHT
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			
			// ALLOW END POINT THAT WON'T REQUIRED JWT TOKEN
			.antMatchers("/api/v1/users/auth/**").permitAll()
			
			// MARK ANY OTHER REQUEST SHOULD BE AUTHENTICATED
			.anyRequest().authenticated();
			
	    return httpSecurity.build();
	  }
	
	/**
	 * Responsible to create authentication object by authenticating user via UsernamePasswordAuthenticationToken object with user name and password
	 * It probably used while login/signin 
	 * Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	 * UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptPasswordEncoder()
	 * PasswordEncoder interface to encode or transform passwords to facilitate the secure storage of the credentials
	 * @return encrypted/encode password based on hashing function 
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	/**
	 * Bean Creation method which holds building a bean with CORS configuration.
	 * SpringSecurity will look for bean name "corsConfigurationSource" for CORS Configuration.
	 * 
	 * @return source {@link CorsConfigurationSource}
	 */
	@Bean
	protected UrlBasedCorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();

		// IF allowedDomains IS NOT EMPTY OR NULL THEN ALLOW ONLY CONFIGURED DOMAIN ELSE ALL DOMAIN
		corsConfiguration.setAllowedOrigins(Arrays.asList(CorsConfiguration.ALL));

		// ALLOWED METHOD TYPES ARE: HEAD, GET, POST, PUT, DELETE, PATCH, OPTIONS
		corsConfiguration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

		// ALL HEADERS ARE ALLOWED
		corsConfiguration.setAllowedHeaders(Arrays.asList(CorsConfiguration.ALL));

		// EXPOSED HEADERS CONFIGURATION
		corsConfiguration.setExposedHeaders(Arrays.asList("X-Auth-Token", "Authorization", 
				"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));

		// FOR ALL REQUEST ABOVE CORS CONFIG WILL BE APPLIED
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	
	/**
	 * FilterRegistrationBean for CorsFilter which is marked as Order 0 means it will first tackle the request.
	 * 
	 * @return filterBean
	 */
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		final UrlBasedCorsConfigurationSource source = corsConfigurationSource();
		FilterRegistrationBean<CorsFilter> filterBean = new FilterRegistrationBean<>(new CorsFilter(source));
		filterBean.setOrder(0);
		return filterBean;
	}
}
