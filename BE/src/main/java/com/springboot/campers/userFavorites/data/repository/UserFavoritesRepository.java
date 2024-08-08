package com.springboot.campers.userFavorites.data.repository;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import com.springboot.campers.userFavorites.data.entity.UserFavoritesId;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserFavoritesRepository extends JpaRepository<UserFavorites, UserFavoritesId> {
    List<UserFavorites> findByUser(UserInfo user);

    @Transactional
    void deleteByUserAndCamp(UserInfo user, CampDefaultInfo camp);
}
