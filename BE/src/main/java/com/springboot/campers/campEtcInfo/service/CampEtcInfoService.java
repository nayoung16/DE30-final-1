package com.springboot.campers.campEtcInfo.service;

import com.springboot.campers.campEtcInfo.data.dao.CampEtcInfoDao;
import com.springboot.campers.campEtcInfo.data.dto.CampEtcInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface CampEtcInfoService {
    CampEtcInfoDto getCampEtcById(Integer contentId) throws Exception;
}
