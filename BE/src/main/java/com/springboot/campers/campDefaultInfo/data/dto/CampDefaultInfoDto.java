package com.springboot.campers.campDefaultInfo.data.dto;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampDefaultInfoDto {
    private Integer contentId;
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

    public CampDefaultInfo toEntity() {
        CampDefaultInfo campDefaultInfo = new CampDefaultInfo();
        campDefaultInfo.setContentId(this.contentId);
        campDefaultInfo.setFacltNm(this.facltNm);
        campDefaultInfo.setLineIntro(this.lineIntro);
        campDefaultInfo.setIntro(this.intro);
        campDefaultInfo.setHvofBgnde(this.hvofBgnde);
        campDefaultInfo.setHvofEnddle(this.hvofEnddle);
        campDefaultInfo.setInduty(this.induty);
        campDefaultInfo.setLctCl(this.lctCl);
        campDefaultInfo.setDoNm(this.doNm);
        campDefaultInfo.setSigunguNm(this.sigunguNm);
        campDefaultInfo.setAddr1(this.addr1);
        campDefaultInfo.setAddr2(this.addr2);
        campDefaultInfo.setMapX(this.mapX);
        campDefaultInfo.setMapY(this.mapY);
        campDefaultInfo.setDirection(this.direction);
        campDefaultInfo.setTel(this.tel);
        campDefaultInfo.setHomepage(this.homepage);
        campDefaultInfo.setResveUrl(this.resveUrl);
        campDefaultInfo.setFirstImageUrl(this.firstImageUrl);
        campDefaultInfo.setOpenPdcl(this.openPdcl);
        campDefaultInfo.setOpenDeCl(this.openDeCl);
        campDefaultInfo.setStarAvg(this.starAvg);
        return campDefaultInfo;
    }

    public static CampDefaultInfoDto toDTO(CampDefaultInfo campDefaultInfo){
        return CampDefaultInfoDto.builder()
                .contentId(campDefaultInfo.getContentId())
                .facltNm(campDefaultInfo.getFacltNm())
                .lineIntro(campDefaultInfo.getLineIntro())
                .intro(campDefaultInfo.getIntro())
                .hvofBgnde(campDefaultInfo.getHvofBgnde())
                .hvofEnddle(campDefaultInfo.getHvofEnddle())
                .induty(campDefaultInfo.getInduty())
                .lctCl(campDefaultInfo.getLctCl())
                .doNm(campDefaultInfo.getDoNm())
                .sigunguNm(campDefaultInfo.getSigunguNm())
                .addr1(campDefaultInfo.getAddr1())
                .addr2(campDefaultInfo.getAddr2())
                .mapX(campDefaultInfo.getMapX())
                .mapY(campDefaultInfo.getMapY())
                .direction(campDefaultInfo.getDirection())
                .tel(campDefaultInfo.getTel())
                .homepage(campDefaultInfo.getHomepage())
                .resveUrl(campDefaultInfo.getResveUrl())
                .firstImageUrl(campDefaultInfo.getFirstImageUrl())
                .openPdcl(campDefaultInfo.getOpenPdcl())
                .openDeCl(campDefaultInfo.getOpenDeCl())
                .starAvg(campDefaultInfo.getStarAvg())
                .build();
    }

}
