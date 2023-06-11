package com.example.cmproject.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@EnableWebMvc // Spring MVC를 사용하여 웹 애플리케이션을 구성할 때 사용
@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {
    //이 클래스는 WebMvcConfigurationSupport를 상속하는 구성 클래스입니다. 이 클래스는 Swagger를 구성하기 위한 설정들을 포함합니다.

    private static final String REFERENCE = "Bearer";

    @Bean
    public Docket api() { //이 메서드는 Docket을 생성하는 빈입니다. Docket은 Swagger 문서를 구성하는 핵심 클래스입니다.
        return new Docket(DocumentationType.OAS_30) // 3.0 문서버전으로 세팅
                .groupName("게시판 커뮤니티 프로젝트")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.cmproject")) // com.example.cmproject 패키지의 API들만 문서에 표시하도록 설정
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()) //모든 경로의 API를 문서에 표시하도록 설정
                .build()
                .useDefaultResponseMessages(false) //기본 응답 메시지를 사용하지 않도록 설정
                .apiInfo(apiInfo()) //API 문서의 정보를 설정, apiInfo() 메서드에서 해당 정보를 생성
                .ignoredParameterTypes(AuthenticationPrincipal.class) //AuthenticationPrincipal 타입의 파라미터를 무시하도록 설정
                .securityContexts(List.of(securityContext()))//보안 컨텍스트를 설정, . securityContext() 메서드에서 해당 컨텍스트를 생성
                .securitySchemes(List.of(bearerAuthSecurityScheme())); // 보안 스키마를 설정, bearerAuthSecurityScheme() 메서드에서 해당 스키마를 생성

    }

    private ApiInfo apiInfo() { // API 문서의 정보를 생성하는 메서드
        return new ApiInfoBuilder()
                .title("게시판 커뮤니티 프로젝트")
                .description("게시판 커뮤니티 제작 ")
//                .contact(new Contact("이름","홈페이지","email"))
//                .license("라이센스소유자")
//                .licenseUrl("라이센스URL")
                .version("1.0")
                .build();
    }

    private HttpAuthenticationScheme bearerAuthSecurityScheme() { // JWT를 사용하는 인증 스키마를 생성,
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name(REFERENCE).build(); //REFERENCE 변수에 정의된 "Bearer"를 스키마 이름으로 설정
    }

    //JWT SecurityContext 구성
    private SecurityContext securityContext() { // 메서드는 보안 컨텍스트를 생성, defaultAuth() 메서드를 통해 기본 인증 설정을 적용
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() { // 기본 인증 설정을 생성
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEveryThing"); //  "global" 범위에 대한 "accessEveryThing" 권한을 설정
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference(REFERENCE, authorizationScopes)); //설정한 것을 SecurityReference로 묶어 리스트로 반환
    }
}