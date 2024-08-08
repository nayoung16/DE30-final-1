package com.springboot.campers.userInfo.data.entity;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.heart.data.entity.Heart;
import com.springboot.campers.post.data.entity.Post;
import com.springboot.campers.userAnswer.data.entity.UserAnswer;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userSchedule.data.entity.UserSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserInfo {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String nickName;

    private String pw;

    private String userImg;

    @ManyToOne
    @JoinColumn(name="styleName")
    private CampStyle campStyle;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> userSchedules;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Post> posts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Heart> hearts;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private UserAnswer userAnswer;


    public static UserInfo toEntity(UserInfoDto dto, CampStyleRepository campStyleRepository){
        if(dto.getStyleNm() != null) {
            CampStyle campStyle = campStyleRepository.findById(dto.getStyleNm())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid style ID: " + dto.getStyleNm()));

            return UserInfo.builder()
                    .id(dto.getId())
                    .nickName(dto.getNickName())
                    .pw(dto.getPw())
                    .userImg(dto.getUserImg())
                    .campStyle(campStyle)
                    .build();
        }
        else {
            return UserInfo.builder()
                    .id(dto.getId())
                    .nickName(dto.getNickName())
                    .pw(dto.getPw())
                    .userImg(dto.getUserImg())
                    .campStyle(null)
                    .build();
        }
    }

    public UserInfo(String id, String nickName, String userImg) {
        this.id = id;
        this.nickName = nickName;
        this.userImg = userImg;
        this.pw = "password";
    }
}
