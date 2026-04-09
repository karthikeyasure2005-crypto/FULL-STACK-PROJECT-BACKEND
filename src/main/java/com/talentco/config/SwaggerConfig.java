package com.talentco.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI talentCoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("TalentCo API")
                .description("TalentCo – Connect Clients with Professionals. " +
                             "Full REST API with OTP Auth, Role-Based Access, and Hiring Workflow.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("TalentCo Team")
                    .email("support@talentco.com"))
                .license(new License()
                    .name("MIT License")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter JWT token obtained from /api/auth/login")));
    }
}
