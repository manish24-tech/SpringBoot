# Spring boot cloud API Gateway

Act as middler to route the client request to the concern services.

## purpose: 
		- To reduce a numbers of authentication call to authenticate client request. authenticate onces and forward request n times.
		- To route the client request to concern services and provide the metrics and monitoring of client request flow.
		- To hide the internal architecture of microservices from the client because request flow from abstract layer which is api gateway and that the reason no need to expose microservices endpoint to the client.

## Recommended: 
		* Always use API gateway with load balancer. To manage the traffic in api gateway.
		* Always use Eureka server to register and discover the API gateway and other microservices instances.


## API Getway Routing Strategy:

	-> Routing is a mechanism to identify the microservice based on path to be executed based on prediction.  

	-> Static Routing : Point to single instance of microservice.
		API gateway -> rout client request directly to the corresponding microservice
		
	-> Dynamic Routing: Point to multiple instance of microservice. 
		API gateway reaches to Eureka Server -> get available instance which does not have any traffic -> rout the client request to the corresponding microservices.

## Dependency
```
- spring-cloud-starter-gateway
- spring-cloud-starter-circuitbreaker-reactor-resilience4j
- spring-cloud-starter-netflix-eureka-client
- spring-boot-starter-actuator
- spring-boot-starter-test
- spring-boot-configuration-processor

- spring-boot-starter-validation
- io.jsonwebtoken : jjwt : 0.9.1
- javax.xml.bind : jaxb-api: 
- org.projectlombok: lombok

- org.springframework.cloud : spring-cloud-dependencie : 2021.0.4

```

# JWT Properties
```
jwt:
  secret: testing
  expirationMs: 86400000
  authDisabled: false
  unauthPath: 
  - /api/v1/users/auth/signin
  - /api/v1/users/auth/signup
  - /api/v1/users/auth/forgot-password
  - /api/v1/users/auth/reset-password
  - /api/v1/workspaces/info-unauth
  
```

## Run Spring Boot application
```
mvn spring-boot:run
```

