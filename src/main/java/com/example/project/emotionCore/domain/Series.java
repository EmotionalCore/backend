package com.example.project.emotionCore.domain;

import com.example.project.emotionCore.Service.TagService;
import com.example.project.emotionCore.dto.SeriesModifyDTO;
import com.example.project.emotionCore.dto.SeriesRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

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

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SeriesTag> tags = new HashSet<>();

    public void updateSeries(String title, String description, String type, Set<Tag> tags, MultipartFile image){
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(image == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = image.getContentType();
        }
        this.title = title;
        this.description = description;
        this.type = type;
        if (this.tags == null) {
            this.tags = new HashSet<>();
        } else {
            this.tags.clear(); // 이전 태그 제거
        }

        for (Tag tag : tags) {
            SeriesTag seriesTag = SeriesTag.builder()
                    .series(this)
                    .tag(tag)
                    .build();
            this.tags.add(seriesTag);
        }

        coverImageUrl = id+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
    }

    public void updateSeries(String title, String description, String type, Set<Tag> tags, MultipartFile image, long authorId){
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(image == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = image.getContentType();
        }
        this.title = title;
        this.description = description;
        this.type = type;
        if (this.tags == null) {
            this.tags = new HashSet<>();
        } else {
            this.tags.clear(); // 이전 태그 제거
        }

        for (Tag tag : tags) {
            SeriesTag seriesTag = SeriesTag.builder()
                    .series(this)
                    .tag(tag)
                    .build();
            this.tags.add(seriesTag);
        }


        coverImageUrl = "/"+id+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
        this.authorId = authorId;
    }

}
