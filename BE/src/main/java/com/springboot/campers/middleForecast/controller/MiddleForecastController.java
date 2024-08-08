package com.springboot.campers.middleForecast.controller;

import com.springboot.campers.middleForecast.data.dto.MiddleForecastDto;
import com.springboot.campers.middleForecast.data.dto.MiddleRequestDto;
import com.springboot.campers.middleForecast.data.entity.MiddleForecast;
import com.springboot.campers.middleForecast.service.MiddleForecastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/middleForecast")
public class MiddleForecastController {
    private final MiddleForecastService middleForecastService;

    public MiddleForecastController(MiddleForecastService middleForecastService) {
        this.middleForecastService = middleForecastService;
    }

    @PostMapping("/read")
    public ResponseEntity<?> selectDay(@RequestBody MiddleRequestDto middleRequestDto){
        List<MiddleForecastDto> middleForecastDtoList = null;
        try{
            middleForecastDtoList = middleForecastService.selectDay(middleRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(middleForecastDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body("예외가 발생했습니다." + e.getMessage());
        }
    }
}
