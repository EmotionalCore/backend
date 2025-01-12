package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@Schema(description = "작품에 대한 간략한 정보(index, 작가정보, 제목, 커버이미지)")
public class SeriesPreviewDTO {
    @Schema(description = "index", example = "1")
    private int id;

    @Schema(description = "작가들의 정보")
    private List<AuthorInfoDTO> authorInfos;

    @Schema(description = "작품의 제목", example = "제목이다..!")
    private String title;

    @Schema(description = "작품 커버 이미지", example = "/static/image/cover/p123abcddeded1w3")
    private String coverImageUrl;
}
