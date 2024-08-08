package com.springboot.campers.userSchedule.data.repository;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Integer> {
    @Query("SELECT us FROM UserSchedule us WHERE us.user.id = :userId")
    List<UserSchedule> findByUserId(@Param("userId") String userId);
}
