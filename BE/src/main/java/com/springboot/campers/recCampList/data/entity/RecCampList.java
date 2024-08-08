package com.springboot.campers.recCampList.data.entity;

import com.springboot.campers.recCampList.data.dto.RecCampListDto;
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
public class RecCampList {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recCampId;

    @Column(nullable = false)
    private String doNm;

    @Column(nullable = false)
    private String facltNm;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo user;

    public static RecCampList toEntity(RecCampListDto dto, UserInfoDao userInfoDao) throws Exception {
        UserInfo userInfo = userInfoDao.selectUser(dto.getUserId());
        return RecCampList.builder()
                .doNm(dto.getDoNm())
                .facltNm(dto.getFacltNm())
                .user(userInfo)
                .build();
    }
}

