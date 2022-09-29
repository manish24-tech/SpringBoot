# Insight Services

As we are well aware that the spring boot provides a production-ready application with its feature where we mainly create services in form of "Monolithic" or "Mico" or a combination of both.

In Monolithic, all features are packed together into a single application while in Microservice, all features it-self as an application. One common thing is to provide a mechanism to communicate and exchange the data between services.

As per the real world, any product/application contains features. Those feature act as services in spring boot like user service, product service, order service etc. We make sure that a single service only has a single responsibility with the usage of spring boot features and cloud features like AWS, Azure, and GCP. Seems like cloud technologies have taken heavy work from us. So, that we can focus on only business execution and flow.

The fun fact is when we learn any feature and in that duration either the same features get introduced with different use cases or cloud technologies introduced the same feature with high security.

- Do you know the spring boot security feature is used to manage user authentication and authorize user requests? now a day nobody manages user credentials in an application instead they give complete responsibility to cloud identity providers like AWS Cognito and Azure AD.

- Do you know spring cloud technologies like eureka service discovery, API gateway, and load balancer allow us to manage microservices and provide collaboration between all? but this responsibility is also we would give to cloud technologies like AWS/Azure API gateway.

But we can use spring boot security for some aspects which make sense like cors configuration, prevent from CSRF attack, permit or denying the request mapping path.

Here, We will try to bring those useful spring boot features which make sense in microservice and monolithic architecture.

## Purpose
- Short content in layman's terms and link for a detailed understanding.
- Default and useful configuration for service discovery and API gateway to startup.
- Approximately 50% use of spring boot feature that common to use whenever required.

# Services

## 1. Service Discovery

- Will use the Netflix Eureka server as service discovery.

- Will Register all the monolithic services and their multiple instances - which means we can start/run a single application with different ports.

- We can use it as a discovery server where all the running instances details can be monitored. Not used in the real-time project as it can be managed by cloud technologies like AWS, Azure..etc.
	
## 2. API Gateway

- Will use spring cloud gateway as an API gateway.
	
- Will perform Routes: API gateway reaches to Eureka Server -> get available instance which does not have any traffic -> route the client request to the corresponding microservices.
	
- Will perform filter to execute conditional execution(execute authenticated and unauthenticated requests) before any request execution
	
- Will perform monitoring: actuator to get the metrics and health status
	
- Will perform default circuit breaker with resilience4j to get the advantage of fault-tolerance like proving fallback URI in case of service is not available
	
- We can use with default feature to get the feel of microservice. Not used in the real-time project as it can be managed by cloud technologies like AWS, Azure..etc.
	
## 3. User Service

- Will use spring boot security to manage role-based user authentication.
	
- Will provide registration, login, forgot password, and reset password journey.
	
- We can use it as an authentication service to manage different authentications like JWT token-based authentication or Oauth2-based authentication with cloud technologies like AWS Cognito, Azure AD...etc.

## 4. Workspace Service
	
- Will Manage all small pieces of code, utilities, and configuration which are common to use in any project.
	
- We can use it as a best practice or guidebook to saving more time in real word project

