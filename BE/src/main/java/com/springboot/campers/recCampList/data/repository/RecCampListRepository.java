package com.springboot.campers.recCampList.data.repository;

import com.springboot.campers.recCampList.data.entity.RecCampList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecCampListRepository extends JpaRepository<RecCampList, Integer> {
    @Query("SELECT rc FROM RecCampList rc WHERE rc.user.id = :userId")
    List<RecCampList> getByUser(@Param("userId") String userId);

    @Query("SELECT rc FROM RecCampList rc WHERE rc.doNm = :doNm and rc.user.id = :userId")
    List<RecCampList> getByDoNmAndUser(@Param("userId") String userId, @Param("doNm") String doNm);

    @Modifying
    @Transactional
    @Query("DELETE FROM RecCampList rc WHERE rc.user.id = :userId")
    void deleteByUser(@Param("userId") String userId);
}
