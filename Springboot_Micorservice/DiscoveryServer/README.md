# Spring boot Eureka Server

To register and discover all microservices including API gateway instances.

Annotations:  @EnableEurekaServer

Properties:	
 - server.port=8761
 - eureka.client.register-with-eureka=false
 - eureka.client.fetch-registry=false


## Dependency
```
- spring-cloud-starter-netflix-eureka-client
- spring-boot-starter-test
- org.springframework.cloud : spring-cloud-dependencie : 2021.0.4

```

# Application Properties
```
# Default port to running eureka discovery server
server.port=8761

# Not to register the self-instance(current running instace - Eureka Discovery Server) but discovers other services.
eureka.client.register-with-eureka=false

# As soon as a service registers itself with the server, it'll not fetches the registry and catches it.
eureka.client.fetch-registry=false

management.endpoint.gateway.enabled=true
management.endpoints.web.exposure.include=gateway
```

## Run Spring Boot application
```
mvn spring-boot:run
```

