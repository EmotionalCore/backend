package com.example.project.emotionCore.domain;

import com.example.project.emotionCore.dto.SeriesModifyDTO;
import com.example.project.emotionCore.dto.SeriesRequestDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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

    @Column(nullable = false)
    private String tags;

    public List<String> getTags(){
        if(tags == null || tags.isEmpty()){
            return new ArrayList<>();
        }
        return Arrays.asList(tags.split(", "));
    }

    public void updateSeries(SeriesModifyDTO dto){
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(dto.getImage() == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = dto.getImage().getContentType();
        }
        title = dto.getTitle();
        description = dto.getDescription();
        type = dto.getType();
        tags = dto.getTags();
        coverImageUrl = id+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
    }

    public void updateSeries(SeriesRequestDTO dto, long authorId){
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(dto.getImage() == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = dto.getImage().getContentType();
        }
        title = dto.getTitle();
        description = dto.getDescription();
        type = dto.getType();
        tags = dto.getTags();
        coverImageUrl = id+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
        this.authorId = authorId;
    }


}
