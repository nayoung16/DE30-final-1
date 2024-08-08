package com.springboot.campers.shortForecast.data.dao;

import com.springboot.campers.shortForecast.data.entity.ShortForecast;

import java.util.List;

public interface ShortForecastDao {
    List<ShortForecast> selectDay(String fcstDate) throws Exception;
}
