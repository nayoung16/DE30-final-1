package com.springboot.campers.recCampList.data.dao;

import com.springboot.campers.recCampList.data.entity.RecCampList;

import java.util.List;

public interface RecCampListDao {
    RecCampList save(RecCampList recCampList);
    List<RecCampList> getByUser(String userId);
    List<RecCampList> getByDo(String userId, String doNm);
    void deleteUserList(String userId);
}
