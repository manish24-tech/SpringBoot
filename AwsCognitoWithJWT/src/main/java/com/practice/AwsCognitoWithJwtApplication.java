package com.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import com.practice.configuration.ApplicationConfiguration;
import com.practice.configuration.ApplicationSecurityConfiguration;
import com.practice.configuration.AwsPropertyConfiguration;
import com.practice.configuration.SwaggerConfiguration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties(AwsPropertyConfiguration.class)
@Import(value = {ApplicationConfiguration.class, ApplicationSecurityConfiguration.class, SwaggerConfiguration.class})
public class AwsCognitoWithJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsCognitoWithJwtApplication.class, args);
	}

}
