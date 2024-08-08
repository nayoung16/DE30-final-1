package com.springboot.campers.userSchedule.data.entity;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campDefaultInfo.data.repository.CampDefaultInfoRepository;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.data.repository.UserInfoRepository;
import com.springboot.campers.userSchedule.data.dto.UserScheduleDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleReqWithUserDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSchedule {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer schId;

    @Column(nullable = false)
    private String fromDate;

    @Column(nullable = false)
    private String toDate;

    private String content;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name="contentId", nullable = false)
    private CampDefaultInfo camp;

    public static UserSchedule toEntity
            (UserScheduleDto userScheduleDto,
             CampDefaultInfoRepository campDefaultInfoRepository,
             UserInfoRepository userInfoRepository) {
        CampDefaultInfo campDefaultInfo = campDefaultInfoRepository.findById(userScheduleDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid content Id: "+userScheduleDto.getContentId()));

        UserInfo userInfo = userInfoRepository.findById(String.valueOf(userScheduleDto.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: "+userScheduleDto.getUserId()));
        return UserSchedule.builder()
                .fromDate(userScheduleDto.getFromDate())
                .toDate(userScheduleDto.getToDate())
                .user(userInfo)
                .camp(campDefaultInfo)
                .content(userScheduleDto.getContent())
                .build();
    }
}
