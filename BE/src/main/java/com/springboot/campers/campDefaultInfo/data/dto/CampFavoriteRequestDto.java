package com.springboot.campers.campDefaultInfo.data.dto;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampFavoriteRequestDto {
    private Integer contentId;
    private String facltNm;
    private String addr1;

    private static CampFavoriteRequestDto toDTO(CampDefaultInfo campDefaultInfo){
        return CampFavoriteRequestDto.builder()
                .contentId(campDefaultInfo.getContentId())
                .facltNm(campDefaultInfo.getFacltNm())
                .addr1(campDefaultInfo.getAddr1())
                .build();
    }
}
