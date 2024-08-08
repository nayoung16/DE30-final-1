package com.springboot.campers.userAnswer.data.entity;

import com.springboot.campers.userAnswer.data.dto.UserAnswerDto;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAnswer {
    @Id
    @Column(name="user_id")
    private String userId;

//    @MapsId
//    @OneToOne
//    @JoinColumn(name="user_id", nullable = false)
//    private UserInfo user;

    private Integer gear_amount;

    private Integer conv_facility;

    private Integer activity;

    private Integer companion;

    private Integer nature;

    private Integer transport;

    private Integer comfort;

    private String envrn_filter;

    private String thema_filter;

    private String doNm;

    public static UserAnswer toEntity(UserAnswerDto dto) throws Exception {
        return UserAnswer.builder()
                .userId(dto.getUserId())
                .gear_amount(dto.getGear_amount())
                .conv_facility(dto.getConvenience_facility())
                .activity(dto.getActivity())
                .companion(dto.getCompanion())
                .nature(dto.getNature())
                .transport(dto.getTransport())
                .comfort(dto.getComfort())
                .envrn_filter(dto.getEnvrn_filter())
                .thema_filter(dto.getThema_filter())
                .doNm(dto.getDoNm())
                .build();
    }

}
