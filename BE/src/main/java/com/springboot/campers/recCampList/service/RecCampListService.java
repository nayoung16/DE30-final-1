package com.springboot.campers.recCampList.service;

import com.springboot.campers.recCampList.data.dto.RecCampListDto;

import java.util.List;

public interface RecCampListService {
    RecCampListDto save(RecCampListDto recCampListDto) throws Exception;
    List<RecCampListDto> getByUser(String userId);
    List<RecCampListDto> getByDoNm( String userId, String doNm);
    void deleteUserList(String userId);

}
