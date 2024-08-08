package com.springboot.campers.campEtcInfo.data.repository;

import com.springboot.campers.campEtcInfo.data.entity.CampEtcInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampEtcInfoRepository extends JpaRepository<CampEtcInfo, Integer> {
}
