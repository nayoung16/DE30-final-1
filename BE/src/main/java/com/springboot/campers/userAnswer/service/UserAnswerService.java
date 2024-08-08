package com.springboot.campers.userAnswer.service;

import com.springboot.campers.userAnswer.data.dto.UserAnswerDto;

public interface UserAnswerService {
    UserAnswerDto select(String userId) throws Exception;
    UserAnswerDto create(UserAnswerDto userAnswerDto) throws Exception;
    UserAnswerDto update(UserAnswerDto userAnswerDto) throws Exception;
    void delete(String userId) throws Exception;
}
