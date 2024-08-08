package com.springboot.campers.userInfo.service;


import com.springboot.campers.userInfo.data.dto.UserInfoDto;

public interface UserInfoService {
    UserInfoDto selectUser(String id) throws Exception;
    UserInfoDto createUser(UserInfoDto userInfoDto);
    UserInfoDto updateUserInfo(UserInfoDto userInfoDto) throws Exception;
    UserInfoDto updateUserStyle(String id, String styleNm) throws Exception;
    void deleteUser(String id) throws Exception;
    UserInfoDto login(String email, String password) throws Exception;
    UserInfoDto register(UserInfoDto userInfoDto) throws Exception;
}
