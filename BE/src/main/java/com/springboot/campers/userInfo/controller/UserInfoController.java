package com.springboot.campers.userInfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.campers.campStyle.data.entity.CampStyle;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.googleLogin.util.UserUtil;
import com.springboot.campers.userAnswer.data.dto.UserAnswerDto;
import com.springboot.campers.userAnswer.service.UserAnswerService;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.dto.UserWithStyleResponseDto;
import com.springboot.campers.userInfo.service.S3Service;
import com.springboot.campers.userInfo.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserInfoController {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    private final UserInfoService userInfoService;
    private final UserAnswerService userAnswerService;
    private final CampStyleRepository campStyleRepository;
    private final S3Service s3Service;

    @PostMapping("/create")
    public ResponseEntity<UserInfoDto> createUser(
            @RequestBody UserInfoDto userInfoDto
    ) throws Exception {
        UserInfoDto userInfoDto1 = userInfoService.createUser(userInfoDto);
        UserAnswerDto userAnswerDto = UserAnswerDto.builder()
                .userId(userInfoDto.getId()).gear_amount(null)
                .convenience_facility(null)
                .activity(null)
                .companion(null)
                .nature(null)
                .transport(null)
                .comfort(null).build();
        userAnswerService.create(userAnswerDto);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto1);
    }

    @GetMapping("/select")
    public ResponseEntity<UserWithStyleResponseDto> selectUser() throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        UserInfoDto userInfoDto = userInfoService.selectUser(email);
        if (userInfoDto.getStyleNm() == null) {
            UserWithStyleResponseDto userWithStyleResponseDto
                    = UserWithStyleResponseDto.builder()
                    .id(userInfoDto.getId())
                    .nickName(userInfoDto.getNickName())
                    .pw(userInfoDto.getPw())
                    .userImg(userInfoDto.getUserImg())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(userWithStyleResponseDto);
        } else {
            Optional<CampStyle> campStyle = campStyleRepository.findById(userInfoDto.getStyleNm());
            if (campStyle.isPresent()) {
                UserWithStyleResponseDto userWithStyleResponseDto
                        = UserWithStyleResponseDto.builder()
                        .id(userInfoDto.getId())
                        .nickName(userInfoDto.getNickName())
                        .pw(userInfoDto.getPw())
                        .userImg(userInfoDto.getUserImg())
                        .campStyleName(campStyle.get().getStyleNm())
                        .build();
                return ResponseEntity.status(HttpStatus.OK).body(userWithStyleResponseDto);
            }
        }
        throw new Exception();
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<UserInfoDto> updateUserInfo(
            @RequestBody UserInfoDto userInfoDto
    ) throws Exception {
        UserInfoDto userInfoDto1 = userInfoService.updateUserInfo(userInfoDto);
        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto1);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String email = UserUtil.getCurrentUserEmail();
        logger.debug("Current User Email: " + email);
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        try {
            String imageUrl = s3Service.uploadFile(file, email);
            return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to upload image"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/updateStyle")
    public ResponseEntity<UserWithStyleResponseDto> updateUserStyle(
            @RequestBody String styleNameJson) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> styleNameMap = objectMapper.readValue(styleNameJson, Map.class);
        String styleName = styleNameMap.get("styleNm");

        UserInfoDto userInfoDto1 = userInfoService.updateUserStyle(email, styleName);

        Optional<CampStyle> campStyle = campStyleRepository.findByStyleNm(styleName);
        if (campStyle.isPresent()) {
            UserWithStyleResponseDto userWithStyleResponseDto
                    = UserWithStyleResponseDto.builder()
                    .id(userInfoDto1.getId())
                    .nickName(userInfoDto1.getNickName())
                    .pw(userInfoDto1.getPw())
                    .userImg(userInfoDto1.getUserImg())
                    .campStyleName(campStyle.get().getStyleNm())
                    .build();
            System.out.println("campstyle present");
            return ResponseEntity.status(HttpStatus.OK).body(userWithStyleResponseDto);
        }
        throw new NoSuchElementException("CampStyle not found with name: " + styleName);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        userInfoService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
    }
}
