package com.springboot.campers.middleForecast.service;

import com.springboot.campers.middleForecast.data.dao.MiddleForecastDao;
import com.springboot.campers.middleForecast.data.dto.MiddleForecastDto;
import com.springboot.campers.middleForecast.data.dto.MiddleRequestDto;
import com.springboot.campers.middleForecast.data.entity.MiddleForecast;
import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MiddleForecastServiceImpl implements MiddleForecastService{
    private final MiddleForecastDao middleForecastDao;

    public MiddleForecastServiceImpl(MiddleForecastDao middleForecastDao) {
        this.middleForecastDao = middleForecastDao;
    }

    @Override
    public List<MiddleForecastDto> selectDay(MiddleRequestDto middleRequestDto) throws Exception {
        List<MiddleForecast> middleForecasts = middleForecastDao.selectDay(middleRequestDto.getFcstDate());
        return middleForecasts.stream()
                .map(MiddleForecastDto::toDTO)
                .collect(Collectors.toList());
    }
}
