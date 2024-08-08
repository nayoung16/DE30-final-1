package com.springboot.campers.middleForecast.data.dto;

import com.springboot.campers.middleForecast.data.entity.MiddleForecast;
import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MiddleForecastDto {

    private int mWeatherId;
    private String fcstDate;
    private String rnStAm;
    private String rnStPm;
    private String wfAm;
    private String wfPm;
    private int taMin;
    private int taMax;
    private String regTempId;
    private String regWeatherId;
    private String regNm;
    private String date;

    public static MiddleForecastDto toDTO(MiddleForecast middleForecast) {
        return MiddleForecastDto.builder()
                .mWeatherId(middleForecast.getMWeatherId())
                .fcstDate(middleForecast.getFcstDate())
                .rnStAm(middleForecast.getRnStAm())
                .rnStPm(middleForecast.getRnStPm())
                .wfAm(middleForecast.getWfAm())
                .wfPm(middleForecast.getWfPm())
                .taMax(middleForecast.getTaMax())
                .taMin(middleForecast.getTaMin())
                .regWeatherId(middleForecast.getRegWeatherId())
                .regTempId(middleForecast.getRegTempId())
                .regNm(middleForecast.getRegNm())
                .build();
    }
}