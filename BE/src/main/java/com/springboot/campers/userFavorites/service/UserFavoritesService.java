package com.springboot.campers.userFavorites.service;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.userFavorites.data.dto.UserFavoritesDto;
import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;

import java.util.List;

public interface UserFavoritesService {
    UserFavoritesDto addUserFavorites(String id, Integer contentId) throws Exception;
    List<UserFavoritesDto> getAllUserFavorites(UserInfoDto userDto) throws Exception;
    void removeUserFavorites(String id, Integer contentId) throws Exception;
}
