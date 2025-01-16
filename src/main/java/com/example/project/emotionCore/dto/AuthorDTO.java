package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "작가들의 정보")
@Data
public class AuthorDTO {
    @Schema(description = "index", example = "1")
    private int id;

    @Schema(description = "작가 닉네임", example = "닉네임이다...!")
    private String name;
}
