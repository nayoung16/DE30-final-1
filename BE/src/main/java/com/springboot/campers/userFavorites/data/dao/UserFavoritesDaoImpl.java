package com.springboot.campers.userFavorites.data.dao;

import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import com.springboot.campers.userFavorites.data.repository.UserFavoritesRepository;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class UserFavoritesDaoImpl implements UserFavoritesDao {

    private final UserFavoritesRepository userFavoritesRepository;

    @Override
    public UserFavorites save(UserFavorites userFavorites) {
        System.out.println("Saving UserFavorites: " + userFavorites);
        return userFavoritesRepository.save(userFavorites);
    }

    @Override
    public List<UserFavorites> findByUser(UserInfo user) {
        return userFavoritesRepository.findByUser(user);
    }

    @Override
    public void deleteByUserAndCamp(UserInfo user, CampDefaultInfo camp) {
        userFavoritesRepository.deleteByUserAndCamp(user, camp);
    }
}
