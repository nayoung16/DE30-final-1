package com.springboot.campers.campStyle.data.repository;

import com.springboot.campers.campStyle.data.entity.CampStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CampStyleRepository extends JpaRepository<CampStyle, String> {
    Optional<CampStyle> findByStyleNm(String styleNm);
}
