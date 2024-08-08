package com.springboot.campers.shortForecast.data.entity;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.shortForecast.data.dto.ShortForecastDto;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShortForecast {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sWeatherId;
    @Column(nullable = false)
    private String fcstDate;
    @Column(nullable = false)
    private int rmSt;
    @Column(nullable = false)
    private String regTempId;
    @Column(nullable = false)
    private String regNm;
    @Column(nullable = false)
    private int taMax;
    @Column(nullable = false)
    private int taMin;

    private int sky;

    private int pty;

    private int tmp;

    @Column(nullable = false)
    private String fcstTime;

}
