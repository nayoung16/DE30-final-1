package com.springboot.campers.campDefaultInfo.service;

import com.springboot.campers.campDefaultInfo.data.dao.CampDefaultInfoDao;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultNameDto;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campDefaultInfo.data.repository.CampDefaultInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CampDefaultInfoServiceImpl implements CampDefaultInfoService{

    private CampDefaultInfoDao campDefaultInfoDao;
    private CampDefaultInfoRepository campDefaultInfoRepository;

    @Override
    public CampDefaultInfoDto getCampDefaultById(Integer contentId) throws Exception {
        Optional<CampDefaultInfo> campDefaultInfo = campDefaultInfoDao.getCampDefaultById(contentId);
        if (campDefaultInfo.isPresent()){
            CampDefaultInfo campDefaultInfo1 = campDefaultInfo.get();
            return CampDefaultInfoDto.toDTO(campDefaultInfo1);
        }
        throw new Exception();
    }

    @Override
    public List<CampDefaultInfoDto> getCampByName(String campName) {
        List<CampDefaultInfo> campLists = campDefaultInfoDao.getCampListByName(campName);
        return campLists.stream()
                .map(CampDefaultInfoDto::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampDefaultInfoDto> getCampByRealName(String campName) {
        List<CampDefaultInfo> campLists = campDefaultInfoDao.getCampByRealName(campName);
        return campLists.stream()
                .map(CampDefaultInfoDto::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCampNames() {
        return campDefaultInfoRepository.getCampNames();
    }
}
