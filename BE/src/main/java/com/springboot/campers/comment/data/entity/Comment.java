package com.springboot.campers.comment.data.entity;

import com.springboot.campers.post.data.entity.Post;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name="postId", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    private UserInfo user;
}
