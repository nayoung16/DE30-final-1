package com.springboot.campers.campDefaultInfo.data.dto;

import com.springboot.campers.campEtcInfo.data.dto.CampEtcInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampInfoResponseDto {
    private CampDefaultInfoDto campDefaultInfoDto;
    private CampEtcInfoDto campEtcInfoDto;
}
