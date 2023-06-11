package com.example.cmproject.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Getter //필드에 대한 Getter 메서드를 자동으로 생성
public class JwtExceptionFilter extends OncePerRequestFilter { // Spring Security의 OncePerRequestFilter를 상속받아 JWT 예외 처리를 담당하는 필터
    //이 클래스는 JWT의 유효성을 검사하고 예외에 따라 적절한 응답을 생성하는 역할을 수행한다.

    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    @Builder
    private JwtExceptionFilter(JwtProvider jwtProvider, JwtProperties jwtProperties) {//이 필드들은 생성자나 빌더 패턴을 통해 주입
        this.jwtProvider = jwtProvider;
        this.jwtProperties = jwtProperties;
    }

    public static JwtExceptionFilter of(JwtProvider jwtProvider, JwtProperties jwtProperties) {//of() 메서드는 주입된 JwtProvider와 JwtProperties를 사용하여 JwtExceptionFilter 인스턴스를 생성
        return JwtExceptionFilter.builder()
                .jwtProvider(jwtProvider)
                .jwtProperties(jwtProperties)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //필터의 실제 로직을 수행
        //해당 메서드는 요청이 들어올 때마다 호출되며, JWT의 유효성 검사와 예외 처리를 수행한다.

        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            verificationAccessToken(accessToken);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            //토큰의 유효기간 만료
            setErrorResponse(response, TokenError.EXPIRED_TOKEN);
        } catch (MalformedJwtException | SignatureException e) {
            //유효하지 않은 토큰
            setErrorResponse(response, TokenError.INVALID_TOKEN);
        } catch (NullPointerException | IllegalArgumentException e) {
            // 토큰이 없습니다.
            setErrorResponse(response, TokenError.UNKNOWN_ERROR);
        }
    }

    private void verificationAccessToken(String accessToken) throws MalformedJwtException, ExpiredJwtException, IllegalArgumentException, NullPointerException {
        //전달된 액세스 토큰의 유효성을 검사
        //JWT의 서명을 확인하고, 만료 여부를 판단하여 예외를 던진다.


        // 토큰의 유효성 검사 로직
        accessToken = jwtProvider.extractToken(accessToken);
        Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(accessToken)
                .getBody();
    }


    public void setErrorResponse(HttpServletResponse response, TokenError errorCode) throws IOException {
        //예외 응답을 설정
        //SON 형식의 응답을 생성하고, 상태 코드와 에러 코드 및 메시지를 포함한다.

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("code", errorCode.getCode());
        responseJson.put("message", errorCode.getMessage());
        response.getWriter().print(responseJson);
    }

    @Getter
    @AllArgsConstructor
    public enum TokenError { //토큰 관련 에러 코드와 메시지를 정의

        INVALID_TOKEN("T400", "유효하지 않은 토큰입니다."),
        UNKNOWN_ERROR("T404", "토큰이 존재하지 않습니다."),
        EXPIRED_TOKEN("T401", "만료된 토큰입니다.");

        private final String code;
        private final String message;
    }
}