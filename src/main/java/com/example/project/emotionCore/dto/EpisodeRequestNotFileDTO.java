package com.example.project.emotionCore.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class EpisodeRequestNotFileDTO {
    private long seriesId;

    private String title;

    //private MultipartFile coverImage;

    private String description;

    private String tags;

    private String contents;

    //private List<MultipartFile> images;
}
