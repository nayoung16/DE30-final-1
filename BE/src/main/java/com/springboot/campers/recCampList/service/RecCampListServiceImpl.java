package com.springboot.campers.recCampList.service;

import com.springboot.campers.recCampList.data.dao.RecCampListDao;
import com.springboot.campers.recCampList.data.dto.RecCampListDto;
import com.springboot.campers.recCampList.data.entity.RecCampList;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecCampListServiceImpl implements RecCampListService{
    private final RecCampListDao recCampListDao;
    private final UserInfoDao userInfoDao;
    @Override
    public RecCampListDto save(RecCampListDto recCampListDto) throws Exception {
        RecCampList recCampList = recCampListDao.save(RecCampList.toEntity(recCampListDto,userInfoDao));
        return RecCampListDto.toDto(recCampList);
    }

    @Override
    public List<RecCampListDto> getByUser(String userId) {
        List<RecCampList> recCampLists = recCampListDao.getByUser(userId);
        List<RecCampListDto> recCampListDtos = recCampLists.stream()
                .map(RecCampListDto::toDto)
                .toList();
        return recCampListDtos;
    }

    @Override
    public List<RecCampListDto> getByDoNm(String userId, String doNm) {
        List<RecCampList> recCampLists = recCampListDao.getByDo(userId, doNm);
        List<RecCampListDto> recCampListDtos = recCampLists.stream()
                .map(RecCampListDto::toDto)
                .toList();
        return recCampListDtos;
    }

    @Override
    public void deleteUserList(String userId) {
        recCampListDao.deleteUserList(userId);
    }
}
