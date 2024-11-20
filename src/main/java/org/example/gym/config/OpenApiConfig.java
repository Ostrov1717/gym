package org.example.gym.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.springdoc", "org.example.gym"})
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("Application API")
                        .description("This is a sample Spring application using springdoc-openapi and OpenAPI 3.")
                        .version("1.0")
                        .contact(new Contact().name("Oleksii Kriachko").email("ostrov1717@gmail.com")));
    }
}

