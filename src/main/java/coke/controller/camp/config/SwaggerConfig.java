package coke.controller.camp.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(title = "campingDict", version = "V1"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenAPi(){

        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("CampingDirect Open APi V1")
                .pathsToMatch(paths)
                .build();
    }


}