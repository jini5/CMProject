package com.example.cmproject.jwt;



import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.service.TokenService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Builder    //@Builder 어노테이션을 사용하여 빌더 패턴을 적용하고, @RequiredArgsConstructor 어노테이션을 사용하여 필수적인 의존성을 생성자에 주입된다.
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { //Spring Security의 OncePerRequestFilter를 상속받아 JWT 인증을 처리하는 필터
    //이 클래스는 JWT 토큰을 검사하고 인증 정보를 설정하여 Spring Security의 인증과 권한 부여를 처리한다.


    //JwtProvider와 TokenService 필드를 가지고 있다. 이 필드들은 생성자나 빌더 패턴을 통해 주입된다.
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IllegalArgumentException {
        //필터의 실제 로직을 수행
        //해당 메서드는 요청이 들어올 때마다 호출되며, JWT 토큰을 검사하고 인증 정보를 설정한다.

        String accesstoken = request.getHeader(HttpHeaders.AUTHORIZATION); //요청 헤더에서 액세스 토큰을 추출

        UserDTO.UserAccessDTO userAccessDTO = jwtProvider.tokenToUser(accesstoken);
        //JwtProvider를 사용하여 액세스 토큰을 분석하여 UserAccessDTO 객체를 얻는다.
        //UserAccessDTO는 JWT 토큰에 포함된 사용자 정보를 나타낸다.

        try {
            if (userAccessDTO != null && !tokenService.checkToken(accesstoken)) {
                //객체가 null이 아니고, 토큰 서비스의 checkToken() 메서드를 사용하여 토큰의 유효성을 검사한다.
                //유효한 경우, SecurityContextHolder를 사용하여 사용자의 인증 정보를 설정한다.
                //인증 정보는 UserAccessDTO 객체와 빈 비밀번호, 권한 정보로 구성된다.
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        userAccessDTO,
                        "",
                        userAccessDTO.getAuthorities()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response); //인증 처리가 완료되었으므로, 다음 필터로 요청을 전달

    }
}
