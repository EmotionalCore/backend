package com.example.project.emotionCore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class AuthorPreviewDTO {
    private int id;
    private String name;
    private String description;
    private String coverImageUrl;
}
