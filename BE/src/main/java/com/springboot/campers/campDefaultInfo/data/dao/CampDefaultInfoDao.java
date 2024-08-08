package com.springboot.campers.campDefaultInfo.data.dao;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;

import java.util.List;
import java.util.Optional;

public interface CampDefaultInfoDao {
    Optional<CampDefaultInfo> getCampDefaultById(Integer contentId);
    List<CampDefaultInfo> getCampListByName(String campName);
    List<CampDefaultInfo> getCampByRealName(String campName);

}
