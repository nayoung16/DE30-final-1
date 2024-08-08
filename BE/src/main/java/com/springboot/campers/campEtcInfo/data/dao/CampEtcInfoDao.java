package com.springboot.campers.campEtcInfo.data.dao;

import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;

import java.util.Optional;

public interface CampEtcInfoDao {
    Optional<CampEtcInfo> getCampEtcById(Integer contentId);
}
