package com.springboot.campers.campEtcInfo.data.entity;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class CampEtcInfo {
    @Id
    @Column(nullable = false)
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

    @OneToOne
    @JoinColumn(name = "contentId", referencedColumnName = "contentId", nullable = false)
    private CampDefaultInfo campDefaultInfo;
}
