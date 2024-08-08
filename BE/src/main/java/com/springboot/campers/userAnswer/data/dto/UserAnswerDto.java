package com.springboot.campers.userAnswer.data.dto;

import com.springboot.campers.userAnswer.data.entity.UserAnswer;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAnswerDto {
    private String userId;
    private Integer gear_amount;
    private Integer convenience_facility;
    private Integer activity;
    private Integer companion;
    private Integer nature;
    private Integer transport;
    private Integer comfort;
    private String envrn_filter;
    private String thema_filter;
    private String doNm;

    public static UserAnswerDto toDTO(UserAnswer userAnswer) {
        return UserAnswerDto.builder()
                .userId(userAnswer.getUserId())
                .gear_amount(userAnswer.getGear_amount())
                .convenience_facility(userAnswer.getConv_facility())
                .activity(userAnswer.getActivity())
                .companion(userAnswer.getCompanion())
                .nature(userAnswer.getNature())
                .transport(userAnswer.getTransport())
                .comfort(userAnswer.getComfort())
                .envrn_filter(userAnswer.getEnvrn_filter())
                .thema_filter(userAnswer.getThema_filter())
                .doNm(userAnswer.getDoNm())
                .envrn_filter(userAnswer.getEnvrn_filter())
                .thema_filter(userAnswer.getThema_filter())
                .doNm(userAnswer.getDoNm())
                .build();
    }

    public static UserAnswerDto reqToDTO(String id, UserAnswerRequestDto userAnswerRequestDto) {
        return UserAnswerDto.builder()
                .userId(id)
                .gear_amount(userAnswerRequestDto.getGear_amount())
                .convenience_facility(userAnswerRequestDto.getConvenience_facility())
                .activity(userAnswerRequestDto.getActivity())
                .companion(userAnswerRequestDto.getCompanion())
                .nature(userAnswerRequestDto.getNature())
                .transport(userAnswerRequestDto.getTransport())
                .comfort(userAnswerRequestDto.getComfort())
                .envrn_filter(userAnswerRequestDto.getEnvrn_filter())
                .thema_filter(userAnswerRequestDto.getThema_filter())
                .doNm(userAnswerRequestDto.getDoNm())
                .build();
    }
}
