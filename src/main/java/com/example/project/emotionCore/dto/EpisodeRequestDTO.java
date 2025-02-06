package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
public class EpisodeRequestDTO {
    private long seriesId;

    private String title;

    private String coverImageUrl;

    private String description;

    private String tags;

    private String contents;
}
