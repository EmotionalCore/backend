package com.example.project.emotionCore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NaverCodeRequestDTO {
    private String code;
    private String state;
}
