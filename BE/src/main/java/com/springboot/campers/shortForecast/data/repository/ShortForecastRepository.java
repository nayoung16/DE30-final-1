package com.springboot.campers.shortForecast.data.repository;

import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShortForecastRepository extends JpaRepository<ShortForecast, Integer> {
    @Query("SELECT s FROM ShortForecast s where s.fcstDate = :fcstDate")
    List<ShortForecast> selectDay(@Param("fcstDate") String fcstDate);
}
