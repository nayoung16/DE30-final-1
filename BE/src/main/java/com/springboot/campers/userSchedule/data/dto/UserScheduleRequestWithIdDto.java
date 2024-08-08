package com.springboot.campers.userSchedule.data.dto;

import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserScheduleRequestWithIdDto {

    private Integer schId;
    private String fromDate;
    private String toDate;
    private Integer contentId;
    private String content;

    public static UserScheduleRequestWithIdDto toDTO(UserSchedule userSchedule) {
        return UserScheduleRequestWithIdDto.builder()
                .schId(userSchedule.getSchId())
                .fromDate(userSchedule.getFromDate())
                .toDate(userSchedule.getToDate())
                .contentId(userSchedule.getCamp().getContentId())
                .content(userSchedule.getContent())
                .build();
    }
}
