package com.springboot.campers.userInfo.data.dao;

import com.springboot.campers.userInfo.data.entity.UserInfo;

public interface UserInfoDao {
    UserInfo createUser(UserInfo userInfo);

    UserInfo selectUser(String id) throws Exception;

    UserInfo updateUserInfo(UserInfo userInfo) throws Exception;

    UserInfo updateUserStyle(String id, String styleNm) throws Exception;

    void deleteUser(String id) throws Exception;
}
