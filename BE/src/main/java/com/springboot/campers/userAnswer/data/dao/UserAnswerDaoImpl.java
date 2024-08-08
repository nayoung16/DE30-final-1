package com.springboot.campers.userAnswer.data.dao;

import com.springboot.campers.userAnswer.data.entity.UserAnswer;
import com.springboot.campers.userAnswer.data.repository.UserAnswerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAnswerDaoImpl implements UserAnswerDao {
    private final UserAnswerRepository userAnswerRepository;

    @Autowired
    public UserAnswerDaoImpl(UserAnswerRepository userAnswerRepository) {
        this.userAnswerRepository = userAnswerRepository;
    }

    @Override
    public UserAnswer select(String userId) throws Exception {
        Optional<UserAnswer> userAnswerOptional = userAnswerRepository.findById(userId);
        if(userAnswerOptional.isPresent()) {
            return userAnswerOptional.get();
        }
        throw new Exception();
    }

    @Override
    public UserAnswer update(UserAnswer userAnswer) throws Exception {
        // UserAnswer의 모든 필드를 업데이트
        UserAnswer existingUserAnswer = userAnswerRepository.findById(userAnswer.getUserId())
                .map(existing -> {
                    existing.setGear_amount(userAnswer.getGear_amount());
                    existing.setConv_facility(userAnswer.getConv_facility());
                    existing.setActivity(userAnswer.getActivity());
                    existing.setCompanion(userAnswer.getCompanion());
                    existing.setNature(userAnswer.getNature());
                    existing.setTransport(userAnswer.getTransport());
                    existing.setComfort(userAnswer.getComfort());
                    existing.setEnvrn_filter(userAnswer.getEnvrn_filter());
                    existing.setThema_filter(userAnswer.getThema_filter());
                    existing.setDoNm(userAnswer.getDoNm());
                    return existing;
                }).orElse(userAnswer);  // 없으면 새로운 엔티티로 간주하고 전달받은 엔티티를 사용

        // 데이터베이스에 저장
        return userAnswerRepository.save(existingUserAnswer);
    }


    @Override
    public UserAnswer create(UserAnswer userAnswer) {
        return userAnswerRepository.save(userAnswer);
    }


    @Transactional
    @Override
    public void delete(String userId) throws Exception {
        UserAnswer userAnswer = this.select(userId);
        if (userAnswer != null) {
            userAnswerRepository.delete(userAnswer);
        } else {
            throw new Exception("UserAnswer not found with userId: " + userId);
        }
    }
}
