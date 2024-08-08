package com.springboot.campers.shortForecast.data.dto;

import com.springboot.campers.shortForecast.data.entity.ShortForecast;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortForecastDto {
    private int sWeahterId;
    private String fcstDate;
    private String regId;
    private String regNum;
    private int rmSt;
    private int taMax;
    private int taMin;
    private int sky;
    private int pty;
    private int tmp;
    private String fcstTime;

    public static ShortForecastDto toDTO(ShortForecast shortForecast) {
        return ShortForecastDto.builder()
                .sWeahterId(shortForecast.getSWeatherId())
                .fcstDate(shortForecast.getFcstDate())
                .regId(shortForecast.getRegTempId())
                .regNum(shortForecast.getRegNm())
                .rmSt((shortForecast.getRmSt()))
                .taMax(shortForecast.getTaMax())
                .taMin(shortForecast.getTaMin())
                .sky(shortForecast.getSky())
                .pty(shortForecast.getPty())
                .tmp(shortForecast.getTmp())
                .fcstTime(shortForecast.getFcstTime())
                .build();
    }
}
