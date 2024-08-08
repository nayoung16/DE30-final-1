package com.springboot.campers.userInfo.data.dto;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithStyleResponseDto {
    private String id;
    private String nickName;
    private String pw;
    private String userImg;
    private String campStyleName;
}
