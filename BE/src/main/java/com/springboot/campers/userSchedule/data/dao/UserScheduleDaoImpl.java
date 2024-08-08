package com.springboot.campers.userSchedule.data.dao;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import com.springboot.campers.userSchedule.data.repository.UserScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserScheduleDaoImpl implements UserScheduleDao{
    private final UserScheduleRepository userScheduleRepository;

    @Autowired
    public UserScheduleDaoImpl(UserScheduleRepository userScheduleRepository) {
        this.userScheduleRepository = userScheduleRepository;
    }

    @Override
    public UserSchedule createUserSchedule(UserSchedule userSchedule) {
        return userScheduleRepository.save(userSchedule);
    }

    @Override
    public List<UserSchedule> selectAllSchedules(String userId){
        System.out.println(userId);
        return userScheduleRepository.findByUserId(userId);
    }

    @Override
    public UserSchedule selectEachSchedule(Integer schId) throws Exception {
        Optional<UserSchedule> userSchedule = userScheduleRepository.findById(schId);
        if(userSchedule.isPresent()){
            return userSchedule.get();
        }
        throw new Exception();
    }

    @Override
    public UserSchedule updateSchedule(UserSchedule userSchedule) throws Exception {
        UserSchedule userScheduleOptional = this.selectEachSchedule(userSchedule.getSchId());
        userScheduleOptional.setToDate(userSchedule.getToDate());
        userScheduleOptional.setFromDate(userSchedule.getFromDate());
        userScheduleOptional.setCamp(userSchedule.getCamp());
        userScheduleOptional.setContent(userSchedule.getContent());
        return userScheduleRepository.save(userScheduleOptional);
    }

    @Override
    public void deleteSchedule(Integer schId) throws Exception {
        UserSchedule userSchedule = this.selectEachSchedule(schId);
        userScheduleRepository.delete(userSchedule);
    }
}
