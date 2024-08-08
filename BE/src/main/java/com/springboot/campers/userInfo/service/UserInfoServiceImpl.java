package com.springboot.campers.userInfo.service;

import com.springboot.campers.campStyle.data.repository.CampStyleRepository;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import com.springboot.campers.userInfo.data.dto.UserInfoDto;
import com.springboot.campers.userInfo.data.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoDao userInfoDao;
    private final CampStyleRepository campStyleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoServiceImpl(UserInfoDao userInfoDao, CampStyleRepository campStyleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userInfoDao = userInfoDao;
        this.campStyleRepository = campStyleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfoDto selectUser(String id) throws Exception {
        UserInfo userInfo = userInfoDao.selectUser(id);
        return UserInfoDto.toDTO(userInfo);
    }

    @Override
    public UserInfoDto createUser(UserInfoDto userInfoDto) {
        UserInfo userInfo = UserInfo.toEntity(userInfoDto, campStyleRepository);
        UserInfo createdUser = userInfoDao.createUser(userInfo);
        return UserInfoDto.toDTO(createdUser);
    }

    @Override
    public UserInfoDto updateUserInfo(UserInfoDto userInfoDto) throws Exception {
        UserInfo userInfo1 = UserInfo.toEntity(userInfoDto, campStyleRepository);
        UserInfo updatedUser = userInfoDao.updateUserInfo(userInfo1);
        return UserInfoDto.toDTO(updatedUser);
    }

    @Override
    public UserInfoDto updateUserStyle(String id, String styleNm) throws Exception {
        UserInfo updatedUser = userInfoDao.updateUserStyle(id, styleNm);
        return UserInfoDto.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(String id) throws Exception {
        userInfoDao.deleteUser(id);
    }

    @Override
    public UserInfoDto login(String id, String password) throws Exception {
        Optional<UserInfo> userInfoOptional = Optional.ofNullable(userInfoDao.selectUser(id));
        if (userInfoOptional.isPresent()) {
            UserInfo userInfo = userInfoOptional.get();
            if (passwordEncoder.matches(password, userInfo.getPw())) {
                return UserInfoDto.toDTO(userInfo);
            }
        }
        return null;
    }

    @Override
    public UserInfoDto register(UserInfoDto userInfoDto) throws Exception {
        try {
            // 기존 사용자가 있는지 확인
            if (userInfoDao.selectUser(userInfoDto.getId()) != null) {
                throw new RuntimeException("User already exists");
            }
        } catch (NoSuchElementException e) {
            // 사용자 없으면 정상 등록 진행
        }

        System.out.println("Converting DTO to entity");
        UserInfo userInfo = UserInfo.toEntity(userInfoDto, campStyleRepository);

        System.out.println("Encoding password");
        userInfo.setPw(passwordEncoder.encode(userInfo.getPw()));

        System.out.println("Setting nickname");
        userInfo.setNickName(userInfoDto.getNickName());

        System.out.println("Creating user in the database");
        userInfo = userInfoDao.createUser(userInfo);

        System.out.println("User created successfully");
        return UserInfoDto.toDTO(userInfo);
    }
}