package com.springboot.campers.campEtcInfo.data.dao;

import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;
import com.springboot.campers.campEtcInfo.data.repository.CampEtcInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class CampEtcInfoDaoImpl implements CampEtcInfoDao{
    private final CampEtcInfoRepository campEtcInfoRepository;

    public CampEtcInfoDaoImpl(CampEtcInfoRepository campEtcInfoRepository) {
        this.campEtcInfoRepository = campEtcInfoRepository;
    }

    @Override
    public Optional<CampEtcInfo> getCampEtcById(Integer contentId) {
        return campEtcInfoRepository.findById(contentId);
    }
}
