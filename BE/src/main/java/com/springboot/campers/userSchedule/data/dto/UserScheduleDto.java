package com.springboot.campers.userSchedule.data.dto;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserScheduleDto {
    private Integer schId;
    private String fromDate;
    private String toDate;
    private String userId;
    private Integer contentId;
    private String content;

    public static UserScheduleDto toDTO(UserSchedule userSchedule) {
        return UserScheduleDto.builder()
                .schId(userSchedule.getSchId())
                .fromDate(userSchedule.getFromDate())
                .toDate(userSchedule.getToDate())
                .userId(userSchedule.getUser().getId())
                .contentId(userSchedule.getCamp().getContentId())
                .content(userSchedule.getContent())
                .build();
    }

}
