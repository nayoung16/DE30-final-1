package com.springboot.campers.middleForecast.data.dao;

import com.springboot.campers.middleForecast.data.entity.MiddleForecast;

import java.util.List;

public interface MiddleForecastDao {
    List<MiddleForecast> selectDay(String fcstDate) throws Exception;
}
