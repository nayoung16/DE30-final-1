package com.springboot.campers.userFavorites.controller;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampFavoriteRequestDto;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campDefaultInfo.service.CampDefaultInfoService;
import com.springboot.campers.googleLogin.util.UserUtil;
import com.springboot.campers.userFavorites.data.dto.UserFavoritesDto;
import com.springboot.campers.userFavorites.service.UserFavoritesService;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.service.UserInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/favorites")
public class UserFavoritesController {

    @Autowired
    private UserFavoritesService userFavoritesService;
    @Autowired
    private UserInfoService userInfoService; // 유저 정보를 얻기 위한 서비스
    @Autowired
    private CampDefaultInfoService campDefaultInfoService; // 캠핑장 정보를 얻기 위한 서비스


    //즐겨찾기 추가
    @PostMapping("/add")
    public ResponseEntity<UserFavoritesDto> addFavorite(@RequestBody Integer contentId) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        System.out.println("Received request to add favorite for email: " + email + ", contentId: " + contentId);
        UserFavoritesDto addedFavorite = userFavoritesService.addUserFavorites(email, contentId);
        System.out.println("Added favorite: " + addedFavorite.getContentId());
        System.out.println(addedFavorite);
        return ResponseEntity.ok(addedFavorite);
    }

    //즐겨찾기 삭제
    @PostMapping("/remove")
    public ResponseEntity<Map<String, String>> removeFavorite(@RequestBody Integer contentId) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        userFavoritesService.removeUserFavorites(email, contentId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Favorite removed successfully");
        return ResponseEntity.ok(response);
    }

    // 모든 즐겨찾기 조회 -- 마이페이지에서 쓸거
    @GetMapping("/getFav")
    public ResponseEntity<List<CampFavoriteRequestDto>> getFav() throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        UserInfoDto user = userInfoService.selectUser(email);
        List<UserFavoritesDto> userFavoritesDto = userFavoritesService.getAllUserFavorites(user);

        List<CampFavoriteRequestDto> campFavorites = userFavoritesDto.stream().map(fav -> {
            CampDefaultInfoDto camp = new CampDefaultInfoDto();
            try {
                camp = campDefaultInfoService.getCampDefaultById(fav.getContentId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return CampFavoriteRequestDto.builder()
                    .contentId(camp.getContentId())
                    .facltNm(camp.getFacltNm())
                    .addr1(camp.getAddr1())
                    .build();
        }).toList();
        return ResponseEntity.ok(campFavorites);
    }
}