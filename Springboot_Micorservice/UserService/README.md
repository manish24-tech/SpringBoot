# Spring boot Security Integration

Act as a authentication service to create user and their credentials.

## Dependency
```

- spring-cloud-starter-netflix-eureka-client
- spring-boot-starter-actuator
- spring-boot-starter-test
- spring-boot-starter-data-jpa
- spring-boot-configuration-processor
- spring-boot-starter-mail
- spring-boot-starter-validation
- spring-boot-starter-web
- spring-boot-starter-security

- org.springframework.cloud : spring-cloud-dependencie : 2021.0.4

```

# JWT Properties
```
# Default port to running Workspace
server.port=8085

## Eureka Server Configuration
# 1. serviceId - registered the service with name in Eureka Discovery Server
spring.application.name=USER-SERVICE

# 2. Eureka location - register the service in Eureka Discovery Server
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka

# 3. instance id for eureka server
eureka.instance.instance-id=${spring.application.name}:${random.value}


# database configuration
spring.datasource.url=jdbc:h2:mem:userdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

```

## Run Spring Boot application
```
mvn spring-boot:run
```

