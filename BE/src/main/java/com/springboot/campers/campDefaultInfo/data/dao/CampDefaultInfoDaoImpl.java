package com.springboot.campers.campDefaultInfo.data.dao;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campDefaultInfo.data.repository.CampDefaultInfoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class CampDefaultInfoDaoImpl implements CampDefaultInfoDao{
    @Autowired
    private final CampDefaultInfoRepository campDefaultInfoRepository;
    @Override
    public Optional<CampDefaultInfo> getCampDefaultById(Integer contentId) {
        return campDefaultInfoRepository.findById(contentId);
    }

    @Override
    public List<CampDefaultInfo> getCampListByName(String campName) {
        return campDefaultInfoRepository.findByCampName(campName);
    }

    @Override
    public List<CampDefaultInfo> getCampByRealName(String campName) {
        return campDefaultInfoRepository.findByRealCampName(campName);
    }
}
