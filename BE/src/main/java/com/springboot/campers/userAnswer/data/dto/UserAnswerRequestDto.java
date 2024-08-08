package com.springboot.campers.userAnswer.data.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAnswerRequestDto {
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
}
