package com.springboot.campers.middleForecast.data.repository;

import com.springboot.campers.middleForecast.data.entity.MiddleForecast;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MiddleForecastRepository extends JpaRepository<MiddleForecast,Integer> {
    @Query("SELECT s FROM MiddleForecast s where s.fcstDate = :fcstDate")
    List<MiddleForecast> selectDay(@Param("fcstDate") String fcstDate);
}
