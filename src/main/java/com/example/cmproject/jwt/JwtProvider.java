package com.example.cmproject.jwt;


import com.example.cmproject.dto.TokenDTO;
import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component// 해당 클래스가 Spring의 컴포넌트로 등록됨을 나타내는 어노테이션
@RequiredArgsConstructor //필드들을 기반으로 생성자를 자동으로 생성해주는 Lombok 어노테이션
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public TokenDTO makeJwtToken(User user) {
        //주어진 User 객체를 기반으로 JWT 토큰을 생성
        //발급된 액세스 토큰과 리프레시 토큰을 TokenDTO 객체로 반환
        //이 클래스는 JWT 토큰의 생성, 해석, 추출 등 다양한 작업을 수행하는 역할을 담당한다.

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer()) // 누가 발급했나.?
                .setIssuedAt(now) //토큰 발급시간
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(300).toMillis())) // 토큰 발급 시간 기준 얼마나 유지 시킬건지.
                .claim("email", user.getEmail()) // 페이로드에 현재 엔티티의 정보
                .claim("role", user.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer()) // 누가 발급했나.?
                .setIssuedAt(now) //토큰 발급시간
                .setExpiration(new Date(now.getTime() + Duration.ofDays(15).toMillis())) // 토큰 발급 시간 기준 얼마나 유지 시킬건지.
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();

        return TokenDTO.builder().accessToken(accessToken).refreshToken(refreshToken).role(user.getRole()).build();
    }


    public UserDTO.UserAccessDTO tokenToUser(String accessToken) {
        //주어진 액세스 토큰을 해석하여 사용자 정보를 포함한 UserDTO.UserAccessDTO 객체로 변환

        try {
            accessToken = extractToken(accessToken);
            Claims claims = null;
            claims = tokenToClaims(accessToken);
            return new UserDTO.UserAccessDTO(claims);
        } catch (Exception e) {
            return null;
        }
    }

    public Claims tokenToClaims(String accessToken) {
        //주어진 액세스 토큰을 해석하여 JWT의 클레임(claim)을 추출한 Claims 객체를 반환
        //jwt필터에서 시큐리티 필터로 넘어가기전에 토큰을 시큐리티 필터가 알수 있게 바꿔준다고 생각하면됨.

        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(accessToken)
                .getBody();

    }

    public String extractToken(String authorizationHeader) {
        //주어진 인증 헤더에서 토큰 부분만 추출하여 반환

        return authorizationHeader.substring(jwtProperties.getTokenPrefix().length());
    }

    public Long getExpiration(String accessToken) {
        // 주어진 액세스 토큰의 만료 시간까지 남은 시간을 밀리초 단위로 반환

        Date expiration =
                Jwts.parser()
                        .setSigningKey(jwtProperties.getSecretKey())
                        .parseClaimsJws(accessToken)
                        .getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public String recreationAccessToken(String userEmail, String userRole) {
        //주어진 사용자 이메일과 역할을 기반으로 새로운 액세스 토큰을 생성하여 반환

        Date now = new Date();
        //Access Token
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer()) // 누가 발급했나.?
                .setIssuedAt(now) //토큰 발급시간
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // 토큰 발급 시간 기준 얼마나 유지 시킬건지.
                .claim("email", userEmail)
                .claim("role", userRole)// 페이로드에 현재 엔티티의 정보
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }


}
