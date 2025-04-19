package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EpisodeResponseDTO {
    private long seriesId;

    private long number;

    private String title;

    private String coverImageUrl;

    private String description;

    private String contents;

    private Long viewCount;

    private LocalDateTime createdAt;
}
