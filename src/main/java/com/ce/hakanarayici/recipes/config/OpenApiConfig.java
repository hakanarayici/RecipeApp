package com.ce.hakanarayici.recipes.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi receipeApiGroup(){
        return GroupedOpenApi.builder()
                .group("Receipes")
                .pathsToMatch("/api/**")
                .packagesToScan("com.ce.hakanarayici.recipes")
                .build();
    }

}
