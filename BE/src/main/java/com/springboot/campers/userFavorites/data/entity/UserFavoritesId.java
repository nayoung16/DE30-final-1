package com.springboot.campers.userFavorites.data.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoritesId implements Serializable {
    private Integer contentId;
    private String id;
}
