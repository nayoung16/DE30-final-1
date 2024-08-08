package com.springboot.campers.userFavorites.data.entity;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@IdClass(UserFavoritesId.class)
public class UserFavorites {

    @Id
    @Column(name = "contentId")
    private Integer contentId;

    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "contentId", insertable = false, updatable = false)
    private CampDefaultInfo camp;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private UserInfo user;
}
