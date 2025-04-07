package com.example.project.emotionCore.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SeriesRequestDTO {
    private String title;

    private String description;

    private String type;

    private String tags;

    private MultipartFile image;
}
