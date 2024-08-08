package com.springboot.campers.userFavorites.data.dao;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import com.springboot.campers.userInfo.data.entity.UserInfo;

import java.util.List;

public interface UserFavoritesDao {
    UserFavorites save(UserFavorites userFavorites);
    List<UserFavorites> findByUser(UserInfo user);
    void deleteByUserAndCamp(UserInfo user, CampDefaultInfo camp);
}
