package com.example.project.emotionCore.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "작품에 대한 상세 정보")
@Data
public class SeriesDetailDTO {
    @Schema(description = "index", example = "1")
    private int id;

    /*
    * *이건 뭘까요?
    * */
    @Schema(description = "작가들의 정보")
    private List<AuthorDTO> authorInfos;

    @Schema(description = "작품의 제목", example = "제목이다..!")
    private String title;

    @Schema(description = "작품 커버 이미지", example = "/static/img/cover/p123abcddeded1w3")
    private String coverImageUrl;

    @Schema(description = "작품 설명", example = "작품 설명입니다... 주절주절")
    private String description;

    @Schema(description = "작품 타입", example = "웹툰")
    private String type;

    @Schema(description = "조회수", example = "123")
    private int viewCount;

    @Schema(description = "좋아요 수", example = "12")
    private int likeCount;

    @Schema(description = "북마크 수", example = "24")
    private int bookmarkCount;

    @Schema(description = "태그들", example = "[\"판타지\", \"마법\"]")
    private List<String> tags;
}
