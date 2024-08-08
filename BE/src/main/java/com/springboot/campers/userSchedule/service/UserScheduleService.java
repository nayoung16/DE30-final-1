package com.springboot.campers.userSchedule.service;

import com.springboot.campers.userSchedule.data.dto.UserScheduleDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleReqWithUserDto;

import java.util.List;

public interface UserScheduleService {
    UserScheduleDto createUserSchedule(UserScheduleDto userScheduleDto);
    List<UserScheduleDto> selectAllSchedules(String userId);
    UserScheduleDto selectEachSchedule(Integer schId) throws Exception;
    UserScheduleDto updateSchedule(UserScheduleDto userScheduleDto) throws Exception;
    void deleteSchedule(Integer schId) throws Exception;
}
