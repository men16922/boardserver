package com.fastcampus.boardserver.controller;

import com.fastcampus.boardserver.dto.UserDTO;
import com.fastcampus.boardserver.dto.request.UserDeleteId;
import com.fastcampus.boardserver.dto.request.UserLoginRequest;
import com.fastcampus.boardserver.dto.request.UserUpdatePasswordRequest;
import com.fastcampus.boardserver.dto.response.LoginResponse;
import com.fastcampus.boardserver.dto.response.UserInfoResponse;
import com.fastcampus.boardserver.service.impl.UserServiceImpl;
import com.fastcampus.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserServiceImpl userService;
    private static LoginResponse loginResponse;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = (UserServiceImpl) userService;
    }

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDto) {
        if (userDto.hasNullDataBeforeSignup(userDto)) {
            throw new RuntimeException("회원가입 정보를 확인해주세요.");
        }
        userService.register(userDto);
    }

    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest userLoginRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseResponseEntity = null;
        String id = userLoginRequest.getUserId();
        String password = userLoginRequest.getPassword();
        LoginResponse loginResponse;
        UserDTO userInfo = userService.login(id, password);

        if (userInfo == null) {
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) {
            loginResponse = LoginResponse.success(userInfo);
            if (userInfo.getStatus() == (UserDTO.Status.ADMIN)) {
                SessionUtil.setLoginAdminId(session, id);
            } else {
                SessionUtil.setLoginMemberId(session, id);
            }
            responseResponseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지원되지 않는 유저입니다.");
        }
        return HttpStatus.OK;
    }

    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session) {
        String id = SessionUtil.getLoginMemberId(session);
        if (id == null) id = SessionUtil.getLoginAdminId(session);
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    @PutMapping("logout")
    public void logout(String accountId, HttpSession session) {
        SessionUtil.clear(session);
    }

    @PatchMapping("password")
    public ResponseEntity<LoginResponse> updateUserPassword(String accountId, @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest, HttpSession session) {
        ResponseEntity<LoginResponse> responseEntity = null;
        String id = SessionUtil.getLoginMemberId(session);
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            userService.updatePassword(id, beforePassword, afterPassword);
            ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException e) {
            log.error("updatePassword 실패", e);
            responseEntity = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @DeleteMapping("delete")
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session) {
        ResponseEntity<LoginResponse> responseEntitiy = null;
//        String id = SessionUtil.getLoginMemberId(session);
        String id = userDeleteId.getId();
        log.info("CHECKER - {}", userDeleteId);
        try {
            userService.deleteId(id, userDeleteId.getPassword());
            responseEntitiy = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("deleteId 실패");
            responseEntitiy = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
        }
        return responseEntitiy;
    }
}
