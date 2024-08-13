package com.springboot.campers.userAnswer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.service.CampDefaultInfoService;
import com.springboot.campers.googleLogin.util.UserUtil;
import com.springboot.campers.recCampList.data.dto.RecCampListDto;
import com.springboot.campers.recCampList.service.RecCampListService;
import com.springboot.campers.userAnswer.data.dto.CombinedResponseDto;
import com.springboot.campers.userAnswer.data.dto.UserAnswerDto;
import com.springboot.campers.userAnswer.data.dto.UserAnswerRequestDto;
import com.springboot.campers.userAnswer.data.dto.UserStyleDto;
import com.springboot.campers.userAnswer.service.UserAnswerService;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@RestController
@RequestMapping("/answer")
public class UserAnswerController {
    private final UserAnswerService userAnswerService;
    private final UserInfoService userInfoService;
    private final RecCampListService recCampListService;
    private final CampDefaultInfoService campDefaultInfoService;

    @Value("${myapp.lambda.url}")
    private String url;

    public UserAnswerController(UserAnswerService userAnswerService, UserInfoService userInfoService, RecCampListService recCampListService, CampDefaultInfoService campDefaultInfoService) {
        this.userAnswerService = userAnswerService;
        this.userInfoService = userInfoService;
        this.recCampListService = recCampListService;
        this.campDefaultInfoService = campDefaultInfoService;
    }

    @GetMapping("/select")
    public ResponseEntity<UserAnswerDto> selectAnswer(
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        UserAnswerDto userAnswerDto1 = userAnswerService.select(email);
        return ResponseEntity.status(HttpStatus.OK).body(userAnswerDto1);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAnswer(
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        try {
            userAnswerService.delete(email);
            return ResponseEntity.status(HttpStatus.OK).body("정상적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body("예외가 발생했습니다.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAnswer(
            @RequestBody UserAnswerRequestDto userAnswerRequestDto
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        try {
            System.out.println("URL: " + url); // URL 확인 로그 추가
            RestTemplate restTemplate = new RestTemplate();

            // 설정된 시간 내에 요청을 완료하기 위해 타임아웃 설정 추가
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectionRequestTimeout(30000); // 연결 요청 타임아웃
            factory.setConnectTimeout(30000); // 연결 타임아웃

            // UTF-8 인코딩 설정
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 엔티티 생성
            HttpEntity<UserAnswerRequestDto> requestEntity = new HttpEntity<>(userAnswerRequestDto, headers);

            // POST 요청 보내기
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody != null) {
                    System.out.println("Response Body: " + responseBody);

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode responseJson = objectMapper.readTree(responseBody);

                    String recommendedStyle = responseJson.path("recommended_style").asText(null);
                    JsonNode recommendedCampsNode = responseJson.path("recommended_camps");


                    if (recommendedStyle != null) {
                        System.out.println("Recommended Style: " + recommendedStyle);

                        // 유저 선택지 update
                        UserAnswerDto userAnswerDto = UserAnswerDto.reqToDTO(email, userAnswerRequestDto);
                        System.out.println(userAnswerDto);
                        userAnswerService.update(userAnswerDto);
//
//                        // 유저 스타일 update
                        UserInfoDto userInfoDto1 = userInfoService.updateUserStyle(email, recommendedStyle);
                        UserStyleDto user = UserStyleDto.builder()
                                .userId(email)
                                .styleNm(userInfoDto1.getStyleNm()).build();
                        // 이전에 추천받은 캠핑장 목록들 삭제
                        recCampListService.deleteUserList(email);
                        // 유저 캠핑장 정보 저장
                        if (recommendedCampsNode.isArray()) {
                            for (JsonNode campNode : recommendedCampsNode) {
                                String camp = campNode.asText();
                                RecCampListDto recCampListDto = RecCampListDto.builder()
                                        .userId(email)
                                        .doNm(userAnswerRequestDto.getDoNm())
                                        .facltNm(camp)
                                        .build();

                                recCampListService.save(recCampListDto);
                            }
                        }
                        List<RecCampListDto> recCampListDtos = recCampListService.getByUser(email);
                        List<CampDefaultInfoDto> campDefaultInfoDtos = new ArrayList<>(); // 리스트 초기화

                        if (recCampListDtos != null) { // Null 검사
                            recCampListDtos.forEach(recCamp -> {
                                List<CampDefaultInfoDto> campDefaultInfoDtos2 = campDefaultInfoService.getCampByRealName(recCamp.getFacltNm());
                                if (campDefaultInfoDtos2 != null) { // 내부 리스트에 대한 Null 검사
                                    campDefaultInfoDtos.addAll(campDefaultInfoDtos2);
                                }
                            });
                        }

                        CombinedResponseDto combinedResponseDto = CombinedResponseDto.builder()
                                .user(user)
                                .camps(campDefaultInfoDtos)
                                .build();

                        return ResponseEntity.status(HttpStatus.OK).body(combinedResponseDto);

                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("recommended_style not found in response");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Empty response body");
                }
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Error fetching recommendation");
            }
        } catch (HttpStatusCodeException e) {
            // HTTP 상태 코드 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(e.getStatusCode()).body("HTTP Status Code Exception: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // 일반 예외 처리
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error fetching recommendation2: " + e.getMessage());
        }
    }
}
