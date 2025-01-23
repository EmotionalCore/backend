package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "작품에 대한 간략한 정보(index, 작가정보, 제목, 커버이미지)")
public class SeriesPreviewDTO {
    @Schema(description = "index", example = "1")
    private int id;

    @Schema(description = "작가의 번호", example ="1")
    private int author_id;

    @Schema(description = "작가의 이름", example="유승헌")
    private String author_name;

    @Schema(description = "작품의 제목", example = "제목이다..!")
    private String title;

    @Schema(description = "작품 커버 이미지", example = "/static/image/cover/p123abcddeded1w3")
    private String coverImageUrl;

    public SeriesPreviewDTO(Series series) {
        this.id = series.getId();
        this.author_id = series.getAuthorInfos().getId().intValue();
        this.author_name = series.getAuthorInfos().getUsername();
        this.title = series.getTitle();
        this.coverImageUrl = series.getCoverImageUrl();
    }
}
