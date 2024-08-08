package com.springboot.campers.campDefaultInfo.service;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultNameDto;

import java.util.List;
import java.util.Optional;

public interface CampDefaultInfoService {
    CampDefaultInfoDto getCampDefaultById(Integer contentId) throws Exception;
    List<CampDefaultInfoDto> getCampByName(String campName);
    List<CampDefaultInfoDto> getCampByRealName(String campName);
    List<String> getCampNames();
}
