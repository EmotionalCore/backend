package com.example.project.emotionCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "작가들의 정보")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewAuthorDTO {

    private Long authorId;
    private String userName;

    private String profileImageUrl;

    private Long episodeCount;
    private LocalDateTime latestCreatedAt;

}
