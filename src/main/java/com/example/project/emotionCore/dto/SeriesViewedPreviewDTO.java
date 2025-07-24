package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "작품에 대한 간략한 정보(시리즈id, 작가정보, 제목, 커버이미지)")
public class SeriesViewedPreviewDTO {
    @Schema(description = "시리즈 id", example = "1")
    private long id;

    @Schema(description = "작가 index", example = "55")
    private long authorId;

    @Schema(description = "작가 닉네임", example = "고양이작가")
    private String authorName;

    @Schema(description = "작품의 제목", example = "제목이다..!")
    private String title;

    @Schema(description = "작품 커버 이미지", example = "/static/img/cover/p123abcddeded1w3")
    private String coverImageUrl;

    @Schema(description = "Episode 몇화인지", example = "1")
    private long episodeNumber;
}
