package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class EpisodeModifyDTO {
    private long seriesId;

    private long number;

    private String title;

    private MultipartFile coverImage;

    private String description;

    private Set<String> tags;

    private String contents;

    private List<MultipartFile> images;
}
