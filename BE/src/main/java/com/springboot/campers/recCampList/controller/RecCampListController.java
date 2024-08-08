package com.springboot.campers.recCampList.controller;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import com.springboot.campers.campDefaultInfo.service.CampDefaultInfoService;
import com.springboot.campers.googleLogin.util.UserUtil;
import com.springboot.campers.recCampList.data.dto.RecCampListDto;
import com.springboot.campers.recCampList.data.dto.RecCampRequestDto;
import com.springboot.campers.recCampList.data.entity.RecCampList;
import com.springboot.campers.recCampList.service.RecCampListService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recList")
@AllArgsConstructor
public class RecCampListController {
    private final RecCampListService recCampListService;
    private final CampDefaultInfoService campDefaultInfoService;

    @GetMapping("/user")
    public ResponseEntity<List<CampDefaultInfoDto>> getUserRecCamp(
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        List<RecCampListDto> recCampListDtos = recCampListService.getByUser(email);
        List<CampDefaultInfoDto> campDefaultInfoDtos = null;

        for (RecCampListDto recCamp : recCampListDtos) {
            List<CampDefaultInfoDto> campDefaultInfoDtos2 = campDefaultInfoService.getCampByRealName(recCamp.getFacltNm());
            campDefaultInfoDtos.addAll(campDefaultInfoDtos2);
        }
        return ResponseEntity.status(HttpStatus.OK).body(campDefaultInfoDtos);
    }

    @GetMapping("/userAndDo")
    public ResponseEntity<List<RecCampListDto>> getUserDoRecCamp(
            @RequestBody String doNm
            ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        List<RecCampListDto> recCampListDtos = recCampListService.getByDoNm(
                email, doNm
        );
        return ResponseEntity.status(HttpStatus.OK).body(recCampListDtos);
    }

    @PostMapping("/save")
    public ResponseEntity<RecCampListDto> saveRecCamp(
            @RequestBody RecCampRequestDto recCampRequestDto
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        RecCampListDto recCampListDto = RecCampListDto.builder()
                .userId(email)
                .doNm(recCampRequestDto.getDoNm())
                .facltNm(recCampRequestDto.getFacltNm())
                .build();
        RecCampListDto campListDto = recCampListService.save(recCampListDto);
        return ResponseEntity.status(HttpStatus.OK).body(campListDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteRecCamp(
    ) {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        recCampListService.deleteUserList(email);
        return ResponseEntity.status(HttpStatus.OK).body("deleted successfully");
    }

}
