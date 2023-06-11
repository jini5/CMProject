package com.example.cmproject.global.config;

import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Spring Security에서 접근 거부(Access Denied)가 발생했을 때 처리하기 위한 커스텀 핸들러
//AccessDeniedHandler 인터페이스를 구현하여 해당 기능을 구현
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //메서드는 접근 거부 예외가 발생했을 때 호출되어 처리를 담당
        //HttpServletRequest와 HttpServletResponse를 전달받고, AccessDeniedException을 처리

        setErrorResponse(response, accessDeniedException.getMessage()); //setErrorResponse() 메서드를 호출하여 응답을 설정
    }

    public void setErrorResponse(HttpServletResponse response, String message) throws IOException { //실제로 응답을 설정하는 로직을 수행

        response.setContentType("application/json;charset=UTF-8"); //응답의 컨텐츠 타입을 JSON으로 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); //HTTP 응답 상태 코드를 403 Forbidden으로 설정
        JSONObject responseJson = new JSONObject(); //JSON 객체를 생성
        responseJson.put("message", message); //JSON 객체에 메시지를 추가
        response.getWriter().print(responseJson); //JSON 객체를 응답으로 출력
    }

    //CustomAccessDeniedHandler 클래스는 접근 거부 예외가 발생했을 때, JSON 형식의 응답을 생성하여 클라이언트에게 전달한다.
    //메시지는 AccessDeniedException에서 가져와 응답에 포함시킬 수 있다.
}
