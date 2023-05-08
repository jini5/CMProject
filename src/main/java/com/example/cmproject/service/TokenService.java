package com.example.cmproject.service;


import com.example.cmproject.dto.TokenDTO;
import org.springframework.http.ResponseEntity;

public interface TokenService {

    ResponseEntity<?> logout(String header, TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO);

    ResponseEntity<?> validateRefreshToken(String refreshToken);

    boolean checkToken(String token);
}
