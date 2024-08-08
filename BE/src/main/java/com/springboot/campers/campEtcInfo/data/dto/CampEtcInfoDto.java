package com.springboot.campers.campEtcInfo.data.dto;

import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampEtcInfoDto {
    private Integer contentId;
    private Integer gnrlSiteCo;
    private Integer autoSiteCo;
    private Integer glampSiteCo;
    private Integer caravSiteCo;
    private String tooltip;
    private String glampInnerFcity;
    private String caravInnerFcity;
    private String trierAcmpnyAt;
    private String caravAcmpnyAt;
    private String sbrsCl;
    private String sbrsEtc;
    private String posblFcltyCl;
    private String posblFcltyEtc;
    private String clturEventEtc;
    private String clturEventAt;
    private String clturEvent;
    private String exprnProgrmAt;
    private String exprnProgrm;
    private String themaEnvrnCl;
    private String eqpmnLendCl;
    private String animalCmgCl;

    public static CampEtcInfoDto toDTO(CampEtcInfo campEtcInfo){
        return CampEtcInfoDto.builder()
                .contentId(campEtcInfo.getContentId())
                .gnrlSiteCo(campEtcInfo.getGnrlSiteCo())
                .autoSiteCo(campEtcInfo.getAutoSiteCo())
                .glampSiteCo(campEtcInfo.getGlampSiteCo())
                .caravSiteCo(campEtcInfo.getCaravSiteCo())
                .tooltip(campEtcInfo.getTooltip())
                .glampInnerFcity(campEtcInfo.getGlampInnerFcity())
                .caravInnerFcity(campEtcInfo.getCaravInnerFcity())
                .trierAcmpnyAt(campEtcInfo.getTrierAcmpnyAt())
                .caravAcmpnyAt(campEtcInfo.getCaravAcmpnyAt())
                .sbrsCl(campEtcInfo.getSbrsCl())
                .sbrsEtc(campEtcInfo.getSbrsEtc())
                .posblFcltyCl(campEtcInfo.getPosblFcltyCl())
                .posblFcltyEtc(campEtcInfo.getPosblFcltyEtc())
                .clturEventEtc(campEtcInfo.getClturEventEtc())
                .clturEventAt(campEtcInfo.getClturEventAt())
                .clturEvent(campEtcInfo.getClturEventEtc())
                .exprnProgrmAt(campEtcInfo.getExprnProgrmAt())
                .exprnProgrm(campEtcInfo.getExprnProgrm())
                .themaEnvrnCl(campEtcInfo.getThemaEnvrnCl())
                .eqpmnLendCl(campEtcInfo.getEqpmnLendCl())
                .animalCmgCl(campEtcInfo.getAnimalCmgCl())
                .build();
    }
}
