package com.springboot.campers.middleForecast.data.dao;

import com.springboot.campers.middleForecast.data.entity.MiddleForecast;
import com.springboot.campers.middleForecast.data.repository.MiddleForecastRepository;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import com.springboot.campers.shortForecast.data.repository.ShortForecastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class MiddleForecastDaoImpl implements MiddleForecastDao{
    private final MiddleForecastRepository middleForecastRepository;
    @Autowired
    public MiddleForecastDaoImpl(MiddleForecastRepository middleForecastRepository) {
        this.middleForecastRepository = middleForecastRepository;
    }

    @Override
    public List<MiddleForecast> selectDay(String fcstDate) throws Exception {
        System.out.println(fcstDate);
        List<MiddleForecast> middleForecasts = middleForecastRepository.selectDay(fcstDate);
        if (middleForecasts.isEmpty()){
            throw new Exception("Forecast date" + fcstDate + "not found");
        }
        return middleForecasts;
    }
}
