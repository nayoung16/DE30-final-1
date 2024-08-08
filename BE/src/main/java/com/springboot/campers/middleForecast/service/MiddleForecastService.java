package com.springboot.campers.middleForecast.service;

import com.springboot.campers.middleForecast.data.dto.MiddleForecastDto;
import com.springboot.campers.middleForecast.data.dto.MiddleRequestDto;

import java.util.List;

public interface MiddleForecastService {
    List<MiddleForecastDto> selectDay(MiddleRequestDto middleRequestDto) throws Exception;
}
