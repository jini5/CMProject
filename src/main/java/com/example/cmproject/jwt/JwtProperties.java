package com.example.cmproject.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor //필드들을 기반으로 생성자를 자동으로 생성해주는 Lombok 어노테이션
@Getter //필드들에 대한 게터 메서드를 자동으로 생성해주는 Lombok 어노테이션
@ConstructorBinding //생성자 주입을 위해 사용되는 Spring Configuration Processor의 어노테이션
@Component //해당 클래스가 Spring의 컴포넌트로 등록됨을 나타내는 어노테이션
@ToString //toString() 메서드를 자동으로 생성해주는 Lombok 어노테이션
public class JwtProperties { //JWT 토큰에 사용되는 속성들을 나타내는 클래스
    //이 클래스는 JWT 발급자(issuer), 비밀 키(secret key), 토큰 접두사(token prefix) 등의 정보를 제공한다.

    //@Value 어노테이션은 Spring의 환경 변수에서 해당 키에 대한 값을 가져와 필드에 주입한다.
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
}
