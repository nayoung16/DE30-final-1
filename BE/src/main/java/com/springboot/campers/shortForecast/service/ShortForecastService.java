package com.springboot.campers.shortForecast.service;

import com.springboot.campers.middleForecast.data.dto.MiddleRequestDto;
import com.springboot.campers.middleForecast.data.repository.MiddleForecastRepository;
import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.shortForecast.data.dto.ShortRequestDto;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;

import java.util.List;

public interface ShortForecastService {
    List<ShortForecastDto> selectDay(ShortRequestDto shortRequestDto) throws Exception;
}
