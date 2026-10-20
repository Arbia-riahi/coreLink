package com.ProjectManagement.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI corelinkAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CoreLink – API Documentation")
                        .version("1.0.0")
                        .description(
                                "Documentation officielle de l’API Back-End du projet CoreLink.\n\n" +
                                "Préparée et maintenue par : RIAHI Arbia – Ingénieure QA Automation.\n" +
                                "Certifications : ISTQB v4 • A4Q Selenium FL • OCA Java • PSM I • PSPO I • SFC .\n\n" +
                                "Cette documentation est fournie pour accompagner les équipes QA, Automation Engineers, " +
                                "Développeurs et DevOps dans la compréhension, le test et l’intégration des API."
                        )
                        .contact(new Contact()
                                .name("RIAHI Arbia – QA Automation Engineer")
                                .email("arb.rihi@gmail.com")
                                .url("https://www.linkedin.com/in/riahi-arbia-/") 
                        )
                        .license(new License().name("Documentation interne – CoreLink © 2025"))
                );
    }
}

