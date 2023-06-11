package com.example.cmproject.global.config;

import com.example.cmproject.jwt.JwtExceptionFilter;
import com.example.cmproject.jwt.JwtFilter;
import com.example.cmproject.jwt.JwtProperties;
import com.example.cmproject.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor //필드 주입 방식으로 JwtFilter, JwtProperties, JwtProvider 의존성을 자동으로 주입하기 위한 Lombok 어노테이션, 해당 클래스의 생성자를 자동으로 생성하여 필드를 초기화한다.
@EnableWebSecurity //Spring Security를 활성화하는 어노테이션, 이 어노테이션을 통해 Spring Security 구성을 활성화한다.
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) //메서드 수준의 보안 어노테이션을 사용할 수 있도록 설정, prePostEnabled, securedEnabled, jsr250Enabled 등의 속성을 설정하여 세부적인 보안 설정을 지정가능
public class SecurityConfig {


    private final JwtFilter jwtFilter;
    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;


    private static final String[] PUBLIC_URLS = { //모든 사용자에게 접근이 허용되는 URL 패턴
            "/auth/**", "/categories/**",
            "/comment/*", "/post/*",
            "/page/popular/regions", "/page/popular/products",
            "/page/**",

            /* swagger v3 */
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    private static final String[] ADMIN_URLS = { // "ADMIN" 권한을 가진 사용자에게만 접근이 허용되는 URL 패턴
            "/admin/**"
    };


    @Bean// 비밀번호를 암호화/복호화하는 데 사용되는 PasswordEncoder를 빈으로 생성
    public PasswordEncoder passwordEncoderParser() {//회원 insert 서비스에서 비밀번호 암호화/복호화에 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); //PasswordEncoderFactories.createDelegatingPasswordEncoder()를 호출하여 암호화 방식을 선택가능
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { //SecurityFilterChain을 빈으로 생성하여 Spring Security의 필터 체인을 구성, 다양한 보안 설정을 포함

        return http
                .cors() // CORS (Cross-Origin Resource Sharing) 관련 구성을 추가하는 메서드, 이를 통해 웹 애플리케이션의 다른 도메인에서 리소스에 접근할 수 있도록 허용
                .and()
                .authorizeRequests()
                .mvcMatchers(PUBLIC_URLS).permitAll() //PUBLIC_URLS에 해당하는 URL 패턴에 대해 모든 사용자에게 접근을 허용하는 설정
                .and()
                .authorizeRequests()
                .mvcMatchers(ADMIN_URLS).hasRole("ADMIN")// ADMIN_URLS에 해당하는 URL 패턴에 대해 "ADMIN" 권한을 가진 사용자만 접근을 허용하는 설정
                .and()
                .authorizeRequests()
                .anyRequest().authenticated() //나머지 모든 요청에 대해 인증된 사용자만 접근을 허용하는 설정, 즉, 인증되지 않은 사용자는 나머지 URL에 접근할 수 없다.
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())//접근 거부 상황에서 처리를 담당하는 accessDeniedHandler()를 설정, 사용자 정의 접근 거부 처리 로직을 구현한 CustomAccessDeniedHandler를 반환
                .and()
                .csrf().disable() //CSRF(Cross-Site Request Forgery) 공격을 방지하는 CSRF 토큰 기능을 비활성화하는 설정, CSRF 토큰을 사용하지 않도록 설정
                .httpBasic().disable() //HTTP 기본 인증을 사용하지 않도록 설정 (HTTP 기본 인증은 사용자 이름과 비밀번호를 평문으로 전송하기 때문에 보안에 취약하다.)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 션을 생성하지 않고 상태가 없는(Stateless) 세션 관리 정책을 설정. 즉, 서버는 클라이언트의 상태를 기록하지 않는다.
                .and()
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(JwtExceptionFilter.of(jwtProvider, jwtProperties), UsernamePasswordAuthenticationFilter.class) //JWT 인증 필터 jwtFilter를 UsernamePasswordAuthenticationFilter 이전에 추가하는 설정. 또한, JWT 예외 처리 필터 JwtExceptionFilter도 UsernamePasswordAuthenticationFilter 이전에 추가
                .build()//HttpSecurity 객체의 설정을 빌드하여 SecurityFilterChain을 반환
                ;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { //시큐리티 필터를 제외하고 처리하는 데 사용
        return (web) -> web.ignoring().mvcMatchers(PUBLIC_URLS);// PUBLIC_URLS에 해당하는 URL 패턴은 시큐리티 필터를 거치지 않고 처리
    }

    //Cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() { //CORS(Cross-Origin Resource Sharing) 구성을 설정
        //CORS = 웹 브라우저에서 동일 출처 정책(Same-Origin Policy)를 우회하여 다른 출처(도메인, 프로토콜, 포트)의 리소스에 접근할 수 있게 하는 메커니즘


        var configuration = new CorsConfiguration();

        //모든 원본(*)으로부터의 모든 헤더와 메서드를 허용하도록 구성
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private AccessDeniedHandler accessDeniedHandler() { // 접근 거부 시 처리를 담당하는 AccessDeniedHandler를 생성
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler(); //CustomAccessDeniedHandler는 사용자 정의 접근 거부 처리 로직을 구현한 클래스
        return accessDeniedHandler;
    }

}