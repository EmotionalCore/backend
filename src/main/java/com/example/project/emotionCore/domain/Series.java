package com.example.project.emotionCore.domain;

import com.example.project.emotionCore.dto.SeriesModifyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, name = "view_count")
    private int viewCount;

    @ColumnDefault("0")
    @Column(nullable = false, name = "like_count")
    private int likeCount;

    @Column(nullable = false, name = "bookmark_count")
    private int bookmarkCount;

    @Column(nullable = false)
    private String tags;

    public List<String> getTags(){
        if(tags == null || tags.isEmpty()){
            return new ArrayList<>();
        }
        return Arrays.asList(tags.split(", "));
    }

    public void updateSeries(SeriesModifyDTO dto){
        title = dto.getTitle();
        description = dto.getDescription();
        type = dto.getType();
        tags = dto.getTags();
    }


}
