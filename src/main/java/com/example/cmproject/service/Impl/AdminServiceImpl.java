package com.example.cmproject.service.Impl;


import com.example.cmproject.dto.UserDTO;
import com.example.cmproject.entity.User;
import com.example.cmproject.repository.UserRepository;
import com.example.cmproject.service.AdminService;
import com.example.cmproject.global.response.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static com.example.cmproject.global.config.PageSizeConfig.User_List_Size;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<?> setUserToAdmin(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            user.setRole("ROLE_ADMIN");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> setAdminToUser(String email) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
            user.setRole("ROLE_USER");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> findUserList(int page) {
        try {
            PageRequest pageable = PageRequest.of(page - 1, User_List_Size);
            Page<UserDTO.UserListDto> userList = userRepository.findAll(pageable)
                    .map(UserDTO.UserListDto::new);
            if (userList.getTotalElements() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(new PageResponseDTO(userList), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> findUser(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(IllegalArgumentException::new);
            return new ResponseEntity<>(new UserDTO.UserDetailsForAdmin(user), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateUserInfo(Long userId, UserDTO.PatchUserByAdminReqDTO dto) {
        try {
            if (dto.getDeleteCheck() != null && !dto.getDeleteCheck().equals("withdraw") && !dto.getDeleteCheck().equals("available")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

            if (dto.getUserProfileImage() != null) {
                user.setProfileImage(dto.getUserProfileImage());
            }
            if (dto.getUserNickName() != null) {
                user.setNickName(dto.getUserNickName());
            }
            if (dto.getUserPhoneNumber() != null) {
                user.setPhoneNumber(dto.getUserPhoneNumber());
            }
            if (dto.getUserBirthday() != null) {
                user.setBirthday(dto.getUserBirthday());
            }
            if (dto.getDeleteCheck() != null) {
                user.setDeleteCheck(dto.getDeleteCheck());
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> updatePw(UserDTO.UserAccessDTO userAccessDTO, UserDTO.UpdatePWReqDTO updatePWReqDTO) {
        try {
            User user = userRepository.findByEmail(userAccessDTO.getEmail()).orElseThrow(IllegalArgumentException::new);
            String changePassword = encodingPassword(updatePWReqDTO.getChangePassword());
            user.updatePassword(changePassword);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


    private String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
