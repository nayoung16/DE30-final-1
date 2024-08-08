package com.springboot.campers.heart.data.entity;

import com.springboot.campers.post.data.entity.Post;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Entity
@Getter
@Setter
@ToString
public class Heart {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer heartId;

    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    private UserInfo user;

    @ManyToOne
    @JoinColumn(name="postId", nullable = false)
    private Post post;
}
