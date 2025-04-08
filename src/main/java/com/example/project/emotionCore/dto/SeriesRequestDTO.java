package com.example.project.emotionCore.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class SeriesRequestDTO {
    private String title;

    private String description;

    private String type;

    private Set<String> tags = new HashSet<>();

    private MultipartFile image;
}
