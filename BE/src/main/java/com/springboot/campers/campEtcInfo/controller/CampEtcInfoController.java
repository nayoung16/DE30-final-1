package com.springboot.campers.campEtcInfo.controller;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampInfoResponseDto;
import com.springboot.campers.campDefaultInfo.service.CampDefaultInfoService;
import com.springboot.campers.campEtcInfo.data.dto.CampEtcInfoDto;
import com.springboot.campers.campEtcInfo.data.repository.CampEtcInfoRepository;
import com.springboot.campers.campEtcInfo.service.CampEtcInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//CampDefaultInfoController 테스트용 ETC이지만 반환은 Default
@RestController
@RequestMapping("/campInfo")
@AllArgsConstructor
public class CampEtcInfoController {
    private final CampDefaultInfoService campDefaultInfoService;

    @GetMapping("/get1")
    public ResponseEntity<CampDefaultInfoDto> getCampDefaultInfo(@RequestParam Integer contentId) throws Exception {
        CampDefaultInfoDto campDefaultInfoDto =
                campDefaultInfoService.getCampDefaultById(contentId);
        return ResponseEntity.status(HttpStatus.OK).body(campDefaultInfoDto);
    }
}