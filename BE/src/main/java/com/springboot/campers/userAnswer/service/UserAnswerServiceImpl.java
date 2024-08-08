package com.springboot.campers.userAnswer.service;

import com.springboot.campers.userAnswer.data.dao.UserAnswerDao;
import com.springboot.campers.userAnswer.data.dto.UserAnswerDto;
import com.springboot.campers.userAnswer.data.entity.UserAnswer;
import com.springboot.campers.userInfo.data.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAnswerServiceImpl implements UserAnswerService{
    private final UserAnswerDao userAnswerDao;

    @Autowired
    public UserAnswerServiceImpl(UserAnswerDao userAnswerDao) {
        this.userAnswerDao = userAnswerDao;
    }

    @Override
    public UserAnswerDto select(String userId) throws Exception {
        UserAnswer userAnswer = userAnswerDao.select(userId);
        return UserAnswerDto.toDTO(userAnswer);
    }

    @Override
    public UserAnswerDto create(UserAnswerDto userAnswerDto) throws Exception {
        UserAnswer userAnswer = UserAnswer.toEntity(userAnswerDto);
        UserAnswer createdAnswer = userAnswerDao.create(userAnswer);
        return UserAnswerDto.toDTO(createdAnswer);
    }

    @Override
    public UserAnswerDto update(UserAnswerDto userAnswerDto) throws Exception {
        UserAnswer userAnswer = UserAnswer.toEntity(userAnswerDto);
        UserAnswer createdAnswer = userAnswerDao.update(userAnswer);
        return UserAnswerDto.toDTO(createdAnswer);
    }


    @Override
    public void delete(String userId) throws Exception {
        userAnswerDao.delete(userId);
    }
}
