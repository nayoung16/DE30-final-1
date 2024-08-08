package com.springboot.campers.userSchedule.data.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserScheduleRequestDto {
    private String fromDate;
    private String toDate;
    private Integer contentId;
    private String content;
}
