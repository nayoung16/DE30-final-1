package com.springboot.campers.userAnswer.data.repository;

import com.springboot.campers.userAnswer.data.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, String> {
}
