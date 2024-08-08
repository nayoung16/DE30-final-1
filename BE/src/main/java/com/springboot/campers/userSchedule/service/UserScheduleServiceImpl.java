package com.springboot.campers.userSchedule.service;

import com.springboot.campers.campDefaultInfo.data.dao.CampDefaultInfoDao;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campDefaultInfo.data.repository.CampDefaultInfoRepository;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.data.repository.UserInfoRepository;
import com.springboot.campers.userSchedule.data.dao.UserScheduleDao;
import com.springboot.campers.userSchedule.data.dto.UserScheduleDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleReqWithUserDto;
import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserScheduleServiceImpl implements UserScheduleService{
    private final UserScheduleDao userScheduleDao;
    private final UserInfoDao userInfoDao;
    private final CampDefaultInfoDao campDefaultInfoDao;
    private final UserInfoRepository userInfoRepository;
    private final CampDefaultInfoRepository campDefaultInfoRepository;

    @Autowired
    public UserScheduleServiceImpl(UserScheduleDao userScheduleDao, UserInfoDao userInfoDao, CampDefaultInfoDao campDefaultInfoDao, UserInfoRepository userInfoRepository, CampDefaultInfoRepository campDefaultInfoRepository) {
        this.userScheduleDao = userScheduleDao;
        this.userInfoDao = userInfoDao;
        this.campDefaultInfoDao = campDefaultInfoDao;
        this.userInfoRepository = userInfoRepository;
        this.campDefaultInfoRepository = campDefaultInfoRepository;
    }

    @Override
    public UserScheduleDto createUserSchedule(UserScheduleDto userScheduleDto) {
        UserSchedule userSchedule = UserSchedule.toEntity(
                userScheduleDto, campDefaultInfoRepository, userInfoRepository
        );
        UserSchedule createdSchedule = userScheduleDao.createUserSchedule(userSchedule);
        return UserScheduleDto.toDTO(createdSchedule);
    }

    @Override
    public List<UserScheduleDto> selectAllSchedules(String userId) {
        List<UserSchedule> userSchedules = userScheduleDao.selectAllSchedules(userId);
        return userSchedules.stream()
                .map(UserScheduleDto::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserScheduleDto selectEachSchedule(Integer schId) throws Exception {
        UserSchedule userSchedule = userScheduleDao.selectEachSchedule(schId);
        return UserScheduleDto.toDTO(userSchedule);
    }

    @Override
    public UserScheduleDto updateSchedule(UserScheduleDto userScheduleDto) throws Exception {
        UserInfo userInfo = userInfoDao.selectUser(userScheduleDto.getUserId());
        Optional<CampDefaultInfo> campDefaultInfo = campDefaultInfoDao.getCampDefaultById(userScheduleDto.getContentId());
        if(campDefaultInfo.isPresent()){
            UserSchedule userSchedule = UserSchedule.builder()
                    .schId(userScheduleDto.getSchId())
                    .fromDate(userScheduleDto.getFromDate())
                    .toDate(userScheduleDto.getToDate())
                    .user(userInfo)
                    .camp(campDefaultInfo.get())
                    .content(userScheduleDto.getContent())
                    .build();
            UserSchedule updatedSchedule = userScheduleDao.updateSchedule(userSchedule);
            return UserScheduleDto.toDTO(updatedSchedule);
        }
        throw new Exception();
    }

    @Override
    public void deleteSchedule(Integer schId) throws Exception {
        userScheduleDao.deleteSchedule(schId);
    }
}
