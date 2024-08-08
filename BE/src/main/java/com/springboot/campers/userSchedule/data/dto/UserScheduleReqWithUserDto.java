package com.springboot.campers.userSchedule.data.dto;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserScheduleReqWithUserDto {
    private String fromDate;
    private String toDate;
    private String userId;
    private Integer contentId;
    private String content;

    public static UserScheduleReqWithUserDto toDTO(UserSchedule userSchedule) {
        return UserScheduleReqWithUserDto.builder()
                .fromDate(userSchedule.getFromDate())
                .toDate(userSchedule.getToDate())
                .userId(userSchedule.getUser().getId())
                .contentId(userSchedule.getCamp().getContentId())
                .content(userSchedule.getContent())
                .build();
    }
}
