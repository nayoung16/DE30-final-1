package com.springboot.campers.userAnswer.data.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserStyleDto {
    private String userId;
    private String styleNm;
}
