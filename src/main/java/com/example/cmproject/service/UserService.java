package com.example.cmproject.service;


import com.example.cmproject.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> signup(UserDTO.SignupReqDTO signupReqDTO);

    ResponseEntity<?> login(UserDTO.LoginReqDTO loginReqDTO);

    ResponseEntity<?> checkUserInfo(UserDTO.UserAccessDTO userAccessDTO);

    ResponseEntity<?> updateUser(UserDTO.UserAccessDTO userAccessDTO, UserDTO.UpdateUserReqDTO updateUserReqDTO);

    ResponseEntity<?> deleteUser(UserDTO.UserAccessDTO userAccessDTO, UserDTO.DeleteUserReqDTO deleteUserReqDTO);

    ResponseEntity<?> emailDuplicationCheck(String email);

    String makePassword();

    ResponseEntity<?> sendPwEmail(String userEmail);

    ResponseEntity<?> checkNickName(String nickName);
}
