package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "작가들의 정보")
@Data
public class AuthorDTO {
    @Schema(description = "index", example = "1")
    private Long id;

    @Schema(description = "작가 닉네임", example = "닉네임이다...!")
    private String name;

    @Schema(description = "작가 설명", example = "나는 ~하는 작가입니다.")
    private String description;

    @Schema(description = "작가가 설정한 링크들(인스타같은거?)", example = "https://www.instargram.com/...")
    private String links;

    @Schema(description = "태그들", example = "판타지, 마법")
    private String tags;
}
