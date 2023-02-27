package com.org.epaperintegration.configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@OpenAPIDefinition(servers = {
        @Server(url = "/", description = "Default Server URL")
})
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Contact contact = new Contact();
        contact.setEmail("mluste94@gmail.com");
        contact.setName("Manish Luste");
        contact.setUrl("https://sites.google.com/view/techies-report");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Epaper Service API")
                .contact(contact)
                .version("1.0")
                .description("This API exposes endpoints to manage epaper.")
                .termsOfService("") // define terms and service url link
                .license(mitLicense);

        return new OpenAPI()
                .info(info);
    }
}
