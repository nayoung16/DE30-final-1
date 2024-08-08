package com.springboot.campers.userSchedule.data.dao;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;

import java.util.List;

public interface UserScheduleDao {
    UserSchedule createUserSchedule(UserSchedule userSchedule);
    List<UserSchedule> selectAllSchedules(String userId);
    UserSchedule selectEachSchedule(Integer schId) throws Exception;
    UserSchedule updateSchedule(UserSchedule userSchedule) throws Exception;
    void deleteSchedule(Integer schId) throws Exception;
}
