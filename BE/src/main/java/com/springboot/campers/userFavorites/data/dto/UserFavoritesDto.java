package com.springboot.campers.userFavorites.data.dto;

import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserFavoritesDto {
    private Integer contentId;
    private String id;

    public static UserFavoritesDto toDTO(UserFavorites userFavorites){
        return UserFavoritesDto.builder()
                .id(userFavorites.getUser().getId())
                .contentId(userFavorites.getCamp().getContentId())
                .build();
    }
}
