package com.springboot.campers.campEtcInfo.service;

import com.springboot.campers.campEtcInfo.data.dao.CampEtcInfoDao;
import com.springboot.campers.campEtcInfo.data.dto.CampEtcInfoDto;
import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampEtcInfoServiceImpl implements CampEtcInfoService{

    @Autowired
    private final CampEtcInfoDao campEtcInfoDao;

    @Override
    public CampEtcInfoDto getCampEtcById(Integer contentId) throws Exception {
        Optional<CampEtcInfo> campEtcInfo = campEtcInfoDao.getCampEtcById(contentId);
        if(campEtcInfo.isPresent()) {
            CampEtcInfo campEtcInfo1 = campEtcInfo.get();
            return CampEtcInfoDto.toDTO(campEtcInfo1);
        }
        throw new Exception();
    }
}
