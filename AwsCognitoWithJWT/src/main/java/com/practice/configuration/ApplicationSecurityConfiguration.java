package com.practice.configuration;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.practice.aws.AwsCognitoJwtAuthFilter;
import com.practice.exception.JwtAccessDeniedHandler;
import com.practice.exception.JwtAuthenticationEntryPoint;

/**
 * Responsible to manage the security layer of an application. Each request would be
 * authenticate and authorize in this layer
 * 
 * @author manish.luste
 *
 */
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter;

	private final JwtAuthenticationEntryPoint authenticationErrorHandler;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final AwsPropertyConfiguration awsPropertyConfiguration;
	private String[] unAuthenticatedApi = {};
	private String[] authenticatedApi = {};

	ApplicationSecurityConfiguration(AwsCognitoJwtAuthFilter awsCognitoJwtAuthFilter,
			JwtAuthenticationEntryPoint authenticationErrorHandler, JwtAccessDeniedHandler jwtAccessDeniedHandler,
			AwsPropertyConfiguration awsPropertyConfiguration) {
		this.awsCognitoJwtAuthFilter = awsCognitoJwtAuthFilter;
		this.authenticationErrorHandler = authenticationErrorHandler;
		this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
		this.awsPropertyConfiguration = awsPropertyConfiguration;
		this.unAuthenticatedApi = this.awsPropertyConfiguration.getCommaSeperatedUnAuthenticatedApiString();
		this.authenticatedApi = this.awsPropertyConfiguration.getCommaSeperatedAuthenticatedApiString();
	}

	/* Configure paths and requests that should be ignored by Spring Security */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**").antMatchers("/", "/*.html", "/favicon.ico", "/**/*.html",
				"/**/*.css", "/**/*.js", "/h2-console/**");
	}

	/* Configure security settings */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// CORS configuration - to avoid access-denied issue from cross platform
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setExposedHeaders(Arrays.asList("X-Auth-Token", "Authorization", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));
		httpSecurity.headers().cacheControl();
		httpSecurity.cors().configurationSource(request -> configuration);

		httpSecurity.csrf().disable()
				// Exception handling - Access denied and UnAuthorize
				.exceptionHandling().authenticationEntryPoint(authenticationErrorHandler)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// enable h2-console
				.and().headers().frameOptions().sameOrigin()

				// create no session
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

				// secure and un-secure end points matcher
				.authorizeRequests().antMatchers("**/health").permitAll().antMatchers(this.unAuthenticatedApi)
				.permitAll().antMatchers(this.authenticatedApi).authenticated().and()
				.addFilterBefore(awsCognitoJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
