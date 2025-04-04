package com.example.project.emotionCore.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EpisodePreviewDTO {
    private long seriesId;

    private long number;

    private String title;

    private String coverImageUrl;

    private long viewCount;

    private LocalDateTime createdAt;
}
