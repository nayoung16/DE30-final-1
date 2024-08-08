package com.springboot.campers.userInfo.data.dto;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String id;
    private String nickName;
    private String pw;
    private String userImg;
    private String styleNm;

    public static UserInfoDto toDTO(UserInfo userInfo) {
        if(userInfo.getCampStyle() == null) {
            return UserInfoDto.builder()
                    .id(userInfo.getId())
                    .nickName(userInfo.getNickName())
                    .pw(userInfo.getPw())
                    .userImg(userInfo.getUserImg())
                    .build();
        }
        else {
            return UserInfoDto.builder()
                    .id(userInfo.getId())
                    .nickName(userInfo.getNickName())
                    .pw(userInfo.getPw())
                    .userImg(userInfo.getUserImg())
                    .styleNm(userInfo.getCampStyle().getStyleNm())
                    .build();
        }
    }

    public static UserInfo fromDto(UserInfoDto dto, CampStyleRepository campStyleRepository) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(dto.getId());
        userInfo.setNickName(dto.getNickName());
        userInfo.setPw(dto.getPw());
        userInfo.setUserImg(dto.getUserImg());

        if (dto.getStyleNm() != null) {
            CampStyle campStyle = campStyleRepository.findById(dto.getStyleNm())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid style ID: " + dto.getStyleNm()));
            userInfo.setCampStyle(campStyle);
        }

        return userInfo;
    }

}
