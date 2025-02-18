package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "작가들의 간략한 정보(id, 이름, 묘사, 커버이미지)")
public class AuthorPreviewDTO {
    private int id;
    private String name;
    private String description;
    private String coverImageUrl;
}
