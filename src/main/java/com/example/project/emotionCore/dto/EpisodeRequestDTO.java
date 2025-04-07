package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class EpisodeRequestDTO {
    private long seriesId;

    private String title;

    private String coverImageUrl;

    private String description;

    private Set<String> tags;

    private String contents;

    private List<MultipartFile> images;
}
