package com.calendarioEventos.event_calendar.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SpringSwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API da Aplicação de Eventos")
                        .version("v1")
                        .description("Requisições HTTP's de interação com usuarios e eventos")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Github")
                        .url("https://github.com/Trevizan1203/ProjetoRedeSocial"))
                .tags(
                        Arrays.asList(
                                new Tag().name("Usuarios").description("Requisicoes de Usuarios"),
                                new Tag().name("Login").description("Requisicoes de Login"),
                                new Tag().name("Eventos").description("Requisicoes de Eventos")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
