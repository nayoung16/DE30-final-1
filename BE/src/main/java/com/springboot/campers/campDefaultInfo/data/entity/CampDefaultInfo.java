package com.springboot.campers.campDefaultInfo.data.entity;

import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;
import com.springboot.campers.naverReview.data.entity.NaverReview;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CampDefaultInfo {
    @Id
    @Column(nullable = false)
    private Integer contentId;

    @Column(nullable = false)
    private String facltNm;

    private String lineIntro;

    private String intro;

    private String hvofBgnde;

    private String hvofEnddle;

    private String induty;

    private String lctCl;

    private String doNm;

    private String sigunguNm;

    private String addr1;

    private String addr2;

    private Double mapX;

    private Double mapY;

    private String direction;

    private String tel;

    private String homepage;

    private String resveUrl;

    private String firstImageUrl;

    private String openPdcl;

    private String openDeCl;

    private Double starAvg;

    @OneToOne(mappedBy = "campDefaultInfo", cascade = CascadeType.ALL, optional = false)
    private CampEtcInfo campEtcInfo;

    @OneToMany(mappedBy = "campDefaultInfo")
    private List<NaverReview> naverReview;

}
