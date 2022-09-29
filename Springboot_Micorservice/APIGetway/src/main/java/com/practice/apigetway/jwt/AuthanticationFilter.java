package com.practice.apigetway.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.practice.apigetway.configuration.JWTConfiguration;
import com.practice.apigetway.exception.AuthenticationException;
import com.practice.apigetway.exception.ErrorResponseDTO;

import reactor.core.publisher.Mono;

/**
 * Responsible to filter each request with AbstractGatewayFilterFactory from
 * spring filter chain after authentication but before authorization Every
 * request verified from this filter and decide granted authority of user to be
 * authorization AbstractGatewayFilterFactory provide GatewayFilter.apply()
 * method which is equivalent to OncePerRequestFilter.doFilterInternal()
 * 
 * @author Manish Luste
 *
 */
@RefreshScope
@Component
public class AuthanticationFilter extends AbstractGatewayFilterFactory<AuthanticationFilter.Config> {

	private static final Logger logger = LoggerFactory.getLogger(AuthanticationFilter.class);

	private final JwtUtils jwtUtils;

	private final JWTConfiguration jwtConfiguration;

	public AuthanticationFilter(JwtUtils jwtUtils, JWTConfiguration jwtConfiguration) {
		super(Config.class);
		this.jwtUtils = jwtUtils;
		this.jwtConfiguration = jwtConfiguration;
	}

	public static class Config {
		//Put the configuration properties for your filter here
	}

	@Override
	public GatewayFilter apply(Config config) {

		logger.info("API Gateway : PreFilter Request");

		return ((exchange, chain) -> {
			
			//If you want to build a "pre" filter you need to manipulate the request before calling chain.filter
            ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
			
			System.out.println("Request URI : "+exchange.getRequest().getURI().getPath());
			if (!jwtConfiguration.getAuthDisabled() && jwtConfiguration.isClientRequestSecured(exchange.getRequest())) {
				
				ServerHttpResponse response = exchange.getResponse();
				
				String jwtToken = jwtUtils.getTokenFromHeader(exchange.getRequest());
				
				if (jwtUtils.isAuthTokenEmpty(jwtToken)) {
					byte[] serializedResponse = errorResponse(HttpStatus.FORBIDDEN, exchange.getRequest().getURI().getPath(), "Authentication token is missing in header");
					response.setStatusCode(HttpStatus.FORBIDDEN);
					response.writeWith(Mono.just(response.bufferFactory().wrap(serializedResponse)));
					return response.setComplete();
				
				} else {
					
					try {
						jwtUtils.validateJwtToken(jwtToken);
					} catch (AuthenticationException e) {
						byte[] serializedResponse = errorResponse(HttpStatus.UNAUTHORIZED, exchange.getRequest().getURI().getPath(), e.getLocalizedMessage());
						response.setStatusCode(HttpStatus.UNAUTHORIZED);
						response.writeWith(Mono.just(response.bufferFactory().wrap(serializedResponse)));
						return response.setComplete();
					}
				}
			}
			
			//use builder to manipulate the request
			return chain.filter(exchange.mutate().request(builder.build()).build());
		});
	}

	private byte[] errorResponse(HttpStatus httpStatus, String path, String errorMessage) {
	
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(httpStatus.value(), httpStatus.getReasonPhrase(),
				errorMessage, path);
		return SerializationUtils.serialize(errorResponse);
	}
}
