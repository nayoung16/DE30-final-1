package com.springboot.campers.userFavorites.service;

import com.springboot.campers.campDefaultInfo.data.dao.CampDefaultInfoDao;
import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.userFavorites.data.dao.UserFavoritesDao;
import com.springboot.campers.userFavorites.data.dto.UserFavoritesDto;
import com.springboot.campers.userFavorites.data.entity.UserFavorites;
import com.springboot.campers.userFavorites.data.repository.UserFavoritesRepository;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserFavoritesServiceImpl implements UserFavoritesService {
    private final UserFavoritesDao userFavoritesDao;
    private final CampDefaultInfoDao campDefaultInfoDao;
    private final UserInfoDao userInfoDao;

    @Override
    @Transactional
    public UserFavoritesDto addUserFavorites(String id, Integer contentId) throws Exception {
        System.out.println("addUserFavorites 시작");
        UserInfo user = userInfoDao.selectUser(id);
        if (user == null) {
            throw new Exception("User not found with id: " + id);
        }
        System.out.println("User found: " + user.getId());

        Optional<CampDefaultInfo> campDefaultById = campDefaultInfoDao.getCampDefaultById(contentId);
        if (campDefaultById.isEmpty()) {
            throw new Exception("Camp not found with contentId: " + contentId);
        }
        System.out.println("Camp found: " + campDefaultById.get().getContentId());

        UserFavorites userFavorites = new UserFavorites();
        userFavorites.setContentId(contentId); // 명시적으로 설정
        userFavorites.setId(id); // 명시적으로 설정
        userFavorites.setUser(user);
        userFavorites.setCamp(campDefaultById.get());
        System.out.println("UserFavorites before save: " + userFavorites);

        UserFavorites savedUserFavorites = userFavoritesDao.save(userFavorites);
        System.out.println("UserFavorites saved: " + savedUserFavorites);

        return UserFavoritesDto.toDTO(savedUserFavorites);
    }

    @Override
    public List<UserFavoritesDto> getAllUserFavorites(UserInfoDto userDto) throws Exception {
        UserInfo user = userInfoDao.selectUser(userDto.getId());
        List<UserFavorites> userFavorites = userFavoritesDao.findByUser(user);
        //리스트를 stream으로 변환하고, UserFavorites객체를 Dto로 맵핑해 collector로 Dto를 리스트로 다시 수집
        return userFavorites.stream().map(UserFavoritesDto::toDTO).collect(Collectors.toList());
    }

    @Override
    public void removeUserFavorites(String id, Integer contentId) throws Exception {
        UserInfo user = userInfoDao.selectUser(id);
        List<UserFavorites> userFavorites = userFavoritesDao.findByUser(user);
        for (UserFavorites userFavorite : userFavorites) {
            if (userFavorite.getCamp().getContentId().equals(contentId)) {
                userFavoritesDao.deleteByUserAndCamp(user, userFavorite.getCamp());
            }
        }
    }
}
