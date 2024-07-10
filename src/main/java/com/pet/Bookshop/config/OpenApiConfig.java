package com.pet.Bookshop.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Bookshop API",
                        email = "bookshop@example.com",
                        url = "https://bookshop-api.com"
                ),
                description = "Bookshop API documentation",
                title = "Bookshop API",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local development server",
                        url = "http://localhost:8081"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name="bearerAuth",
        description="JWT authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER

)
@Configuration
public class OpenApiConfig {


}
