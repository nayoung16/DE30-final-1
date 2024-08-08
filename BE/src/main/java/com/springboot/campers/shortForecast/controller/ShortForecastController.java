package com.springboot.campers.shortForecast.controller;

import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.shortForecast.data.dto.ShortRequestDto;
import com.springboot.campers.shortForecast.service.ShortForecastService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shortForecast")
@AllArgsConstructor
public class ShortForecastController {
    private final ShortForecastService shortForecastService;
    @PostMapping("/read")
    public ResponseEntity<?> selectDay(
            @RequestBody ShortRequestDto shortRequestDto){
        List<ShortForecastDto> shortForecastDtoList = null;
        try{
            shortForecastDtoList =shortForecastService.selectDay(shortRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(shortForecastDtoList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body("예외가 발생했습니다.");
        }
    }
}
