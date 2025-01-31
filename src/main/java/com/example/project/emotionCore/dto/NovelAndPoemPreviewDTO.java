package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelAndPoemPreviewDTO{
    private int id;
    private int authorId;
    private String authorName;
    private String title;
    private String description;
    private String coverImageUrl;

}
