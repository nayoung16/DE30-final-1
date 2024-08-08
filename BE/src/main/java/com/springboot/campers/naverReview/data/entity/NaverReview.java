package com.springboot.campers.naverReview.data.entity;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
public class NaverReview {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reviewId;

    @Column(nullable = false)
    private String campNm;

    @Column(nullable = false)
    private Double star;

    @Column(nullable = false)
    private String reviewCont;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    @ManyToOne
    @JoinColumn(name = "contentId",nullable = false)
    private CampDefaultInfo campDefaultInfo;


}
