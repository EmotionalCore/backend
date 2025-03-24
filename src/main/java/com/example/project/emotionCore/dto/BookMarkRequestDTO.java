package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookMarkRequestDTO {

    @Schema(description = "시리즈Id", example = "1")
    private Long seriesId;
}
