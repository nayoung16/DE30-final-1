package com.springboot.campers.userAnswer.data.dto;

import com.springboot.campers.campDefaultInfo.data.dto.CampDefaultInfoDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CombinedResponseDto {
    private List<CampDefaultInfoDto> camps;
    private UserStyleDto user;
}
