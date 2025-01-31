package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelAndPoemPreviewDTO{
    private int id;
    private int authorId;
    private String authorName;
    private String title;
    private String description;
    private String coverImageUrl;

    public NovelAndPoemPreviewDTO(Series series) {
        this.id = series.getId();
        this.authorId = series.getAuthorInfos().getId().intValue();
        this.authorName = series.getAuthorInfos().getUsername(); // AuthorDTO 사용 안 하고 직접 할당
        this.title = series.getTitle();
        this.description = series.getDescription();
        this.coverImageUrl = series.getCoverImageUrl();
    }
}
