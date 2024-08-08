package com.springboot.campers.googleLogin.controller;

import com.google.common.net.HttpHeaders;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.googleLogin.config.JWTUtils;
import com.springboot.campers.googleLogin.dto.IdTokenRequestDto;
import com.springboot.campers.googleLogin.dto.LoginRequestDto;
import com.springboot.campers.googleLogin.service.AccountService;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

@RestController
@RequestMapping("/v1/oauth")
public class LoginController {

    @Autowired
    AccountService accountService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    CampStyleRepository campStyleRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserInfoDto userInfoDto) {
        if (userInfoDto == null || userInfoDto.getId() == null || userInfoDto.getNickName() == null || userInfoDto.getPw() == null) {
            return ResponseEntity.badRequest().body("Invalid user data");
        }

        try {
            UserInfoDto registeredUser = userInfoService.register(userInfoDto);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginWithGoogleOauth2(@RequestBody IdTokenRequestDto requestBody, HttpServletResponse response) {
        try {
            System.out.println("google login 들어옴");
            String authToken = accountService.loginOAuthGoogle(requestBody);
            final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                    .httpOnly(true)
                    .secure(false) // HTTPS를 사용하지 않으면 false로 설정
                    .maxAge(7 * 24 * 3600)
                    .path("/")
                    .sameSite("None") // SameSite 속성 설정
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            System.out.println("login controller에서 생성한 쿠키입니다 : " + cookie);
            Authentication authentication = jwtUtils.verifyAndGetAuthentication(authToken);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authenticated user: {}", authentication.getName());
                logger.info(SecurityContextHolder.getContext().toString());
                System.out.println("Authenticated user" + authentication.getName());
            } else {
                logger.warn("Authentication failed for token: {}", authToken);
                System.out.println(authentication + " 로그인에서 생성된 authentication 입니다");
            }
            return ResponseEntity.ok(Collections.singletonMap("token", authToken));
        } catch (Exception e) {
            System.err.println("Exception in loginWithGoogleOauth2: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    @PostMapping("/localLogin")
    public ResponseEntity<?> loginWithOwnCredentials(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response) throws Exception {
        UserInfoDto userInfoDto = userInfoService.login(loginRequest.getId(), loginRequest.getPw());
        if (userInfoDto == null) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        UserInfo userInfo = UserInfo.toEntity(userInfoDto, campStyleRepository);
        System.out.println("로그인에서 첫번째 jwtUtils 호출 및 authToken 생성");
        String authToken = jwtUtils.createToken(userInfo, false);
        System.out.println("생성된 authToken: " + authToken + "1번 jwtUtils 호출 완료");
        Authentication authentication = jwtUtils.verifyAndGetAuthentication(authToken);
        System.out.println("authToken 기반으로 생성된 authentication 객체 확인 후 시큐리티에 객체 추가, 현재 authentication 객체" + authentication );
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        System.out.println("주입된 authentication 객체: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("authToken 기반으로 쿠키 생성 시작");
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", authToken)
                .httpOnly(true) // 보안 상 httpOnly를 true로 설정하는 것이 좋습니다.
                .maxAge(7 * 24 * 3600)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("생성된 쿠키 확인 :" + cookie);
        return ResponseEntity.ok(Collections.singletonMap("token", authToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        final ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", "")
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }
}