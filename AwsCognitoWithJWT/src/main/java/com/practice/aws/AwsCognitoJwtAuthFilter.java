package com.practice.aws;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.practice.exception.CongitoJWTException;

/**
 * Responsible to filter each authenticated and unAuthenticated requests and
 * validate with JWT token
 * 
 * @author manish.luste
 */
public class AwsCognitoJwtAuthFilter extends GenericFilter {

	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(AwsCognitoJwtAuthFilter.class);

	private transient AwsCognitoIdTokenProcessor cognitoIdTokenProcessor;

	public AwsCognitoJwtAuthFilter(AwsCognitoIdTokenProcessor cognitoIdTokenProcessor) {
		this.cognitoIdTokenProcessor = cognitoIdTokenProcessor;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		Authentication authentication = null;
		String url = ((HttpServletRequest) request).getRequestURL().toString();
		try {
			authentication = this.cognitoIdTokenProcessor.authenticate((HttpServletRequest) request);
			if (authentication != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				logger.warn(
						"AwsCognitoJwtAuthFilter.doFilter(): Authenticated OR Un-Authenticated Request has been processd with url: "
								+ url);
				SecurityContextHolder.clearContext();
			}
		} catch (CongitoJWTException e) {
			logger.error("AwsCognitoJwtAuthFilter.doFilter() : ".concat(e.printDetail()).concat(" : URL: ").concat(url)
					.concat(" With Error: ").concat(e.getMessage()));
			SecurityContextHolder.clearContext();
		}

		filterChain.doFilter(request, response);
	}
}
