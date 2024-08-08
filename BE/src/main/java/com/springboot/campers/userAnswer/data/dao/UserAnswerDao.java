package com.springboot.campers.userAnswer.data.dao;

import com.springboot.campers.userAnswer.data.entity.UserAnswer;

public interface UserAnswerDao {
    UserAnswer select(String userId) throws Exception;
    UserAnswer update(UserAnswer userAnswer) throws Exception;
    UserAnswer create(UserAnswer userAnswer);
    void delete(String userId) throws Exception;
}
