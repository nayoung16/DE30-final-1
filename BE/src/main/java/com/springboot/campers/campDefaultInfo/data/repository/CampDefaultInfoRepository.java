package com.springboot.campers.campDefaultInfo.data.repository;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultNameDto;
import com.springboot.campers.campDefaultInfo.data.entity.CampDefaultInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampDefaultInfoRepository extends JpaRepository<CampDefaultInfo, Integer> {
    @Query("SELECT c FROM CampDefaultInfo c WHERE c.facltNm LIKE %:campName%")
    List<CampDefaultInfo> findByCampName(@Param("campName") String campName);

    @Query("SELECT c FROM CampDefaultInfo c WHERE c.facltNm = :campName")
    List<CampDefaultInfo> findByRealCampName(@Param("campName") String campName);

    @Query("SELECT c.facltNm FROM CampDefaultInfo c")
    List<String> getCampNames();
}
