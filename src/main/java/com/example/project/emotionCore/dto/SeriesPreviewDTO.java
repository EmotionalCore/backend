package com.example.project.emotionCore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class SeriesPreviewDTO {
    private int id;
    private List<AuthorInfoDTO> authorInfos;
    private String title;
    private String coverImageUrl;
}
