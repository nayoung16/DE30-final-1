package com.springboot.campers.middleForecast.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MiddleForecast {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mWeatherId;
    @Column(nullable = false)
    private String fcstDate;
    @Column(nullable = false)
    private String rnStAm;
    @Column(nullable = false)
    private String rnStPm;
    @Column(nullable = false)
    private String wfAm;
    @Column(nullable = false)
    private String wfPm;
    @Column(nullable = false)
    private int taMin;
    @Column(nullable = false)
    private int taMax;
    @Column(nullable = false)
    private String regWeatherId;
    @Column(nullable = false)
    private String regNm;
    @Column(nullable = false)
    private String regTempId;
}
