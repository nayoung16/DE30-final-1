package com.springboot.campers.shortForecast.data.dao;

import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import com.springboot.campers.shortForecast.data.repository.ShortForecastRepository;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ShortForecastDaoImpl implements ShortForecastDao{
    private final ShortForecastRepository shortForecastRepository;
    @Autowired
    public ShortForecastDaoImpl(ShortForecastRepository shortForecastRepository) {
        this.shortForecastRepository = shortForecastRepository;
    }
    @Override
    public List<ShortForecast> selectDay(String fcstDate) throws Exception {
        List<ShortForecast> shortForecasts = shortForecastRepository.selectDay(fcstDate);
        if (shortForecasts.isEmpty()){
            throw new Exception("Forecast date" + fcstDate + "not found");
        }
        return shortForecasts;
    }
}
