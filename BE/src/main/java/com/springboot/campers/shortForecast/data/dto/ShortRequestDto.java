package com.springboot.campers.shortForecast.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ShortRequestDto {
    private String fcstDate;
}
