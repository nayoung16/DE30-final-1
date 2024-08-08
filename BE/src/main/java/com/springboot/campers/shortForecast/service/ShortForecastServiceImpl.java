package com.springboot.campers.shortForecast.service;

import com.springboot.campers.middleForecast.data.dto.MiddleRequestDto;
import com.springboot.campers.shortForecast.data.dao.ShortForecastDao;
import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.shortForecast.data.dto.ShortRequestDto;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortForecastServiceImpl implements ShortForecastService {
    private final ShortForecastDao shortForecastDao;

    public ShortForecastServiceImpl(ShortForecastDao shortForecastDao) {
        this.shortForecastDao = shortForecastDao;
    }

    @Override
    public List<ShortForecastDto> selectDay(ShortRequestDto shortRequestDto) throws Exception {
        List<ShortForecast> shortForecasts = shortForecastDao.selectDay(shortRequestDto.getFcstDate());
        return shortForecasts.stream()
                .map(ShortForecastDto::toDTO)
                .collect(Collectors.toList());
    }
}
