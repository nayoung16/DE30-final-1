package com.springboot.campers.googleLogin.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.springboot.campers.googleLogin.config.JWTUtils;
import com.springboot.campers.googleLogin.dto.IdTokenRequestDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.data.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class AccountService {
    private final UserInfoRepository userInfoRepository;
    private final JWTUtils jwtUtils;
    private final GoogleIdTokenVerifier verifier;

    public AccountService(@Value("${app.googleClientId}") String clientId, UserInfoRepository userInfoRepository,
                          JWTUtils jwtUtils) {
        this.userInfoRepository = userInfoRepository;
        this.jwtUtils = jwtUtils;
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public String loginOAuthGoogle(IdTokenRequestDto requestBody) {
        UserInfo userInfo = verifyIDToken(requestBody.getIdToken());
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        // 기존 사용자 확인
        Optional<UserInfo> existingUserInfo = userInfoRepository.findById(userInfo.getId());
        if (existingUserInfo.isPresent()) {
            UserInfo existingUser = existingUserInfo.get();
            // 기존 사용자 정보 업데이트 (필요한 경우)

            userInfo = userInfoRepository.save(existingUser);
        } else {
            // 새로운 사용자 정보 저장
            userInfo = userInfoRepository.save(userInfo);
        }

        String jwtUtilsToken = jwtUtils.createToken(userInfo, false);
        System.out.println(jwtUtilsToken + " AccountService에서 생성된 토큰입니다");
        return jwtUtilsToken;
    }


    private UserInfo verifyIDToken(String idToken) {
        try {
            GoogleIdToken idTokenObj = verifier.verify(idToken);
            if (idTokenObj == null) {
                return null;
            }
            GoogleIdToken.Payload payload = idTokenObj.getPayload();
            String name = (String) payload.get("name");
            String email = payload.getEmail();
            String pictureUrl = (String) payload.get("picture");

            return new UserInfo(email, name, pictureUrl);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }
}
