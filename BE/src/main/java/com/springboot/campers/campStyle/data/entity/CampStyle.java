package com.springboot.campers.campStyle.data.entity;

import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class CampStyle {
    @Id
    @Column(nullable = false)
    private String styleNm;
}
