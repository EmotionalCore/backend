package com.example.project.emotionCore.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
public class SeriesModifyDTO {
    private Long id;

    private String title;

    private String description;

    private String type;

    private Set<String> tags;

    private MultipartFile image;
}
