package com.poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class SpringBootCrudOperationApplication {

    private String apiVersion = "1.0";
    private static final String API_KEY_HEADER_NAME = "x-api-key";

    public static void main(String[] args) {

		SpringApplication.run(SpringBootCrudOperationApplication.class, args);
	}

    @Bean
    public Docket swaggerSettings() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .select()
                //.apis(RequestHandlerSelectors.basePackage("com.tru"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    private ApiKey apiKey() {
        return new ApiKey(API_KEY_HEADER_NAME, API_KEY_HEADER_NAME, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/anyPath.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference(API_KEY_HEADER_NAME, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot Demo")
                .description("Bucket List Manager")
                //.contact(new Contact("TestName", "http:/test-url.com", "test@test.de"))
                .version(apiVersion)
                .build();
    }
}
