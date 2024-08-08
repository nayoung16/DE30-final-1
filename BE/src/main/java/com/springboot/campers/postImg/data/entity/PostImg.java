package com.springboot.campers.postImg.data.entity;

import com.springboot.campers.post.data.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Entity
@Getter
@Setter
@ToString
public class PostImg {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name="postId", nullable = false)
    private Post post;
}
