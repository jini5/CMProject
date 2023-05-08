package com.example.cmproject.service.Impl;


import com.example.cmproject.dto.TokenDTO;
import com.example.cmproject.jwt.JwtProvider;
import com.example.cmproject.repository.RedisTemplateRepository;
import com.example.cmproject.repository.UserRepository;
import com.example.cmproject.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtProvider jwtProvider;
    private final RedisTemplateRepository redisTemplateRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> logout(String header, TokenDTO.RefreshTokenReqDTO refreshTokenReqDTO) {

        if (checkToken(header)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            try {
                String accessToken = jwtProvider.extractToken(header);
                redisTemplateRepository.setDataExpire(header, "logout", jwtProvider.getExpiration(accessToken));
                jwtProvider.getExpiration(refreshTokenReqDTO.getRefreshToken());
                redisTemplateRepository.deleteData(refreshTokenReqDTO.getRefreshToken());

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public ResponseEntity<?> validateRefreshToken(String refreshToken) {
        try {
            String userEmail = redisTemplateRepository.getData(refreshToken);
            if (jwtProvider.getExpiration(refreshToken) > 0 && checkToken(refreshToken)) {
                //Role 확인
                String userRole = userRepository.findByEmail(userEmail).orElseThrow(IllegalArgumentException::new).getRole();

                String updateAccessToken = jwtProvider.recreationAccessToken(userEmail, userRole);
                return new ResponseEntity<>(TokenDTO.builder().accessToken(updateAccessToken).refreshToken(refreshToken).role(userRole).build(), HttpStatus.OK);
            } else {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public boolean checkToken(String token) {
        return redisTemplateRepository.hasKey(token);
    }

}