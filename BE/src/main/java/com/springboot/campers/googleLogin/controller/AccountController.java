package com.springboot.campers.googleLogin.controller;

import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/oauth")
public class AccountController {

    @Autowired
    UserInfoService userInfoService;

    @GetMapping("/user/info")
    public ResponseEntity<?> getUserInfo() {
        System.out.println("Received request to /user/info");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 인증 객체: " + authentication);

        if (authentication == null || authentication.getName() == null) {
            System.out.println("Authentication is null or authentication name is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        System.out.println("Authentication: " + authentication);
        String userEmail = authentication.getName();
        System.out.println("사용자 이메일: " + userEmail);

        try {
            UserInfoDto userInfo = userInfoService.selectUser(userEmail);
            System.out.println("조회된 사용자 정보: " + userInfo);
            if (userInfo == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(userInfo);
        } catch (Exception e) {
            System.out.println("사용자 정보 조회 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user info: " + e.getMessage());
        }
    }
}

