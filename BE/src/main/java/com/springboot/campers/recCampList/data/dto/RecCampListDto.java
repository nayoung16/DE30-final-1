package com.springboot.campers.recCampList.data.dto;

import com.springboot.campers.recCampList.data.entity.RecCampList;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecCampListDto {
    String doNm;
    String facltNm;
    String userId;

    public static RecCampListDto toDto(RecCampList recCampList) {
        return RecCampListDto.builder()
                .doNm(recCampList.getDoNm())
                .facltNm(recCampList.getFacltNm())
                .userId(recCampList.getUser().getId())
                .build();
    }
}
