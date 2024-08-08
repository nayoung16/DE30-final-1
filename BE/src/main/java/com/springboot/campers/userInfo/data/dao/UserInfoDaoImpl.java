package com.springboot.campers.userInfo.data.dao;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import com.springboot.campers.userInfo.data.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class UserInfoDaoImpl implements UserInfoDao {
    private final UserInfoRepository userInfoRepository;
    private final CampStyleRepository campStyleRepository;

    @Autowired
    public UserInfoDaoImpl(UserInfoRepository userInfoRepository, CampStyleRepository campStyleRepository) {
        this.userInfoRepository = userInfoRepository;
        this.campStyleRepository = campStyleRepository;
    }

    @Override
    public UserInfo createUser(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo selectUser(String id) throws Exception {
        Optional<UserInfo> selectedUser = userInfoRepository.findById(id);
        if (selectedUser.isPresent()) {
            return selectedUser.get();
        }
        throw new NoSuchElementException("User not found with id: " + id);
    }

    @Override
    public UserInfo updateUserInfo(UserInfo userInfo) throws Exception {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userInfo.getId());

        if (userInfoOptional.isPresent()) {
            UserInfo existingUser = userInfoOptional.get();
            existingUser.setNickName(userInfo.getNickName());
            existingUser.setPw(userInfo.getPw());
            existingUser.setUserImg(userInfo.getUserImg());
            existingUser.setCampStyle(userInfo.getCampStyle());
            return userInfoRepository.save(existingUser);
        }
        throw new NoSuchElementException("User not found with id: " + userInfo.getId());
    }

    @Override
    public UserInfo updateUserStyle(String id, String styleNm) throws Exception {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(id);

        if (userInfoOptional.isPresent()) {
            UserInfo existingUser = userInfoOptional.get();
            Optional<CampStyle> campStyleOptional = campStyleRepository.findByStyleNm(styleNm);
            if (campStyleOptional.isPresent()) {
                existingUser.setCampStyle(campStyleOptional.get());
                return userInfoRepository.save(existingUser);
            }
            throw new NoSuchElementException("CampStyle not found with name: " + styleNm);
        }
        throw new NoSuchElementException("User not found with id: " + id);
    }


    @Override
    public void deleteUser(String id) throws Exception {
        UserInfo user = this.selectUser(id);
        userInfoRepository.delete(user);
    }
}
