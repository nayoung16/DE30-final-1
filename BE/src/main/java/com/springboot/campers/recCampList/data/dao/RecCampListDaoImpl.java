package com.springboot.campers.recCampList.data.dao;

import com.springboot.campers.recCampList.data.entity.RecCampList;
import com.springboot.campers.recCampList.data.repository.RecCampListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecCampListDaoImpl implements RecCampListDao{
    private final RecCampListRepository repository;

    @Autowired
    public RecCampListDaoImpl(RecCampListRepository repository) {
        this.repository = repository;
    }

    @Override
    public RecCampList save(RecCampList recCampList) {
        return repository.save(recCampList);
    }

    @Override
    public List<RecCampList> getByUser(String userId) {
        return repository.getByUser(userId);
    }

    @Override
    public List<RecCampList> getByDo(String userId, String doNm) {
        return repository.getByDoNmAndUser(userId, doNm);
    }

    @Override
    public void deleteUserList(String userId) {
        repository.deleteByUser(userId);
    }
}
