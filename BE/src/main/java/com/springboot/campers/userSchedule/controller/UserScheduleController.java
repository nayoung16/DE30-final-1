package com.springboot.campers.userSchedule.controller;

import com.springboot.campers.googleLogin.util.UserUtil;
import com.springboot.campers.userSchedule.data.dto.UserScheduleDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleReqWithUserDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleRequestDto;
import com.springboot.campers.userSchedule.data.dto.UserScheduleRequestWithIdDto;
import com.springboot.campers.userSchedule.service.UserScheduleService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sch")
@AllArgsConstructor
public class UserScheduleController {
    private final UserScheduleService userScheduleService;

    @PostMapping("/create")
    public ResponseEntity<UserScheduleDto> createSchedule(
            @RequestBody UserScheduleRequestDto userScheduleDto
    ) {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        UserScheduleDto userScheduleDto1 = UserScheduleDto.builder()
                .userId(email)
                .fromDate(userScheduleDto.getFromDate())
                .toDate(userScheduleDto.getToDate())
                .contentId(userScheduleDto.getContentId())
                .content(userScheduleDto.getContent())
                .build();
        UserScheduleDto userScheduleDto2 = userScheduleService.createUserSchedule(userScheduleDto1);
        return ResponseEntity.status(HttpStatus.OK).body(userScheduleDto2);
    }

    @GetMapping("/selectAll")
    public ResponseEntity<?> selectAllSchedules() {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        List<UserScheduleDto> userSchedules = null;
        try {
            userSchedules = userScheduleService.selectAllSchedules(email);
            System.out.println(userSchedules);
            return ResponseEntity.status(HttpStatus.OK).body(userSchedules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body("예외가 발생했습니다.");
        }
    }

    @GetMapping("/selectEach")
    public ResponseEntity<?> selectEachSchedule(
            @RequestBody Integer schId
    ) throws Exception {
        UserScheduleDto userScheduleDto = null;
        try {
            userScheduleDto = userScheduleService.selectEachSchedule(schId);
            return ResponseEntity.status(HttpStatus.OK).body(userScheduleDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FOUND).body("예외가 발생했습니다.");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<UserScheduleDto> updateUserInfo(
            @RequestBody UserScheduleRequestWithIdDto userScheduleRequestDto
    ) throws Exception {
        String email = UserUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
        UserScheduleDto userScheduleDto = UserScheduleDto.builder()
                .schId(userScheduleRequestDto.getSchId())
                .fromDate(userScheduleRequestDto.getFromDate())
                .toDate(userScheduleRequestDto.getToDate())
                .userId(email)
                .contentId(userScheduleRequestDto.getContentId())
                .content(userScheduleRequestDto.getContent())
                .build();
        UserScheduleDto userScheduleDto1 = userScheduleService.updateSchedule(userScheduleDto);
        return ResponseEntity.status(HttpStatus.OK).body(userScheduleDto1);
    }

    @DeleteMapping("/delete/{schId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable("schId") Integer schId
    ) {
        try {
            userScheduleService.deleteSchedule(schId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
