package com.example.cmproject.service;


import com.example.cmproject.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<?> setUserToAdmin(String email);

    ResponseEntity<?> setAdminToUser(String email);

    ResponseEntity<?> findUserList(int page);

    ResponseEntity<?> findUser(Long id);

    ResponseEntity<?> updateUserInfo(Long userId, UserDTO.PatchUserByAdminReqDTO dto);

    ResponseEntity<?> updatePw(UserDTO.UserAccessDTO userAccessDTO, UserDTO.UpdatePWReqDTO updatePWReqDTO);
}