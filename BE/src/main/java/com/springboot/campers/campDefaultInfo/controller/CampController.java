package com.springboot.campers.campDefaultInfo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultNameDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampInfoResponseDto;
import com.springboot.campers.campDefaultInfo.service.CampDefaultInfoService;
import com.springboot.campers.campEtcInfo.data.dto.CampEtcInfoDto;
import com.springboot.campers.campEtcInfo.service.CampEtcInfoService;
import com.springboot.campers.userFavorites.data.dto.UserFavoritesDto;
import com.springboot.campers.userFavorites.service.UserFavoritesService;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/campInfo")
@AllArgsConstructor
public class CampController {
    private final CampDefaultInfoService campDefaultInfoService;
    private final CampEtcInfoService campEtcInfoService;

    @PostMapping("/get")
    public ResponseEntity<CampInfoResponseDto> getCombinedCampInfo(@RequestBody Integer contentId) {
        try {
            CampDefaultInfoDto campDefaultInfoDto = campDefaultInfoService.getCampDefaultById(contentId);
            CampEtcInfoDto campEtcInfoDto = campEtcInfoService.getCampEtcById(contentId);

            CampInfoResponseDto responseDto = new CampInfoResponseDto();
            responseDto.setCampDefaultInfoDto(campDefaultInfoDto);
            responseDto.setCampEtcInfoDto(campEtcInfoDto);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/redirect")
    public void getresveUrl(@RequestBody Integer contentId, HttpServletResponse httpServletResponse) throws IOException{
        try {
            CampDefaultInfoDto campDefaultInfoDto = campDefaultInfoService.getCampDefaultById(contentId);
            String resveUrl = campDefaultInfoDto.getResveUrl();
            httpServletResponse.sendRedirect(resveUrl);
        } catch (Exception e) {
            httpServletResponse.sendError(httpServletResponse.SC_NOT_FOUND);
        }
    }
    @PostMapping("/search")
    public ResponseEntity<List<CampDefaultInfoDto>> getCampListsByName(@RequestBody String campName) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(campName);
        String campName1 = jsonNode.get("campName").asText(); //넘어온 json string으로 변환
        try {
            List<CampDefaultInfoDto> campLists = campDefaultInfoService.getCampByName(campName1);
            System.out.println(campLists);
            System.out.println("데이터 전달 잘되는중!!!!!");
            return ResponseEntity.status(HttpStatus.OK).body(campLists);
        } catch (Exception e) {
            System.out.println("데이터 전달 안되는중!!!!!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getCampNames")
    public ResponseEntity<List<String>> getCampNames() {
        List<String> campNames = campDefaultInfoService.getCampNames();
        return ResponseEntity.status(HttpStatus.OK).body(campNames);
    }
}
