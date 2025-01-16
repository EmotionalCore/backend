package com.example.project.emotionCore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class NovelAndPoemPreviewDTO{
    private int id;
    private List<AuthorDTO> authorInfos;
    private String title;
    private String description;
    private String coverImageUrl;
}
