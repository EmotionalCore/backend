package com.example.project.emotionCore.domain;

import com.example.project.emotionCore.Service.TagService;
import com.example.project.emotionCore.dto.EpisodeModifyDTO;
import com.example.project.emotionCore.dto.EpisodeRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity
@Table(name = "episode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@IdClass(Episode.EpisodeKey.class)
public class Episode {
//일단 figma에 있는것들 넣었는데 나중에 사라지면 지우기

    @Id
    @Column(name = "series_id", nullable = false)
    private Long seriesId;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "number", nullable = false)
    private Long number;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "cover_image_url", columnDefinition = "TEXT")
    private String coverImageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count", insertable = false, updatable = true)
    private Long viewCount;

    //@Transient
    //private List<MultipartFile> images;

    public void incrementViewCount() {
        this.viewCount++;
    }

    private void changeFilename(){ //코드 이게 최선인가
        String regex = "\\[\\*IMG&]\\(([^.]+)\\.([^)]+)\\)"; //[*IMG&](?.?)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contents);

        int count = 0;
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String to = "[*IMG&](/" + seriesId + "/" + number + "/" + count + "." + matcher.group(2) + ")";
            matcher.appendReplacement(sb, to);
            count++;
        }
        matcher.appendTail(sb);
        contents = sb.toString();
    }

    public void update(EpisodeRequestDTO dto, TagService tagService){ //코드 개떡 이게 왜 여깄지
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(dto.getCoverImage() == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = dto.getCoverImage().getContentType();
        }
        this.title = dto.getTitle();
        this.coverImageUrl = seriesId+"/"+number+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
        this.description = dto.getDescription();

        /*
        Set<Tag> tagSet = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .collect(Collectors.toSet());
        for (Tag tag : tagSet) {
            EpisodeTag episodeTag = EpisodeTag.builder()
                    .episode(this)
                    .tag(tag)
                    .build();
            this.tags.add(episodeTag);
        }
        */


        this.contents = dto.getContents();
        changeFilename();
    }

    public void update(EpisodeModifyDTO dto, TagService tagService){ //코드 개떡 이게 왜 여깄지
        String coverImageFileType; //코드 ㄹㅇ 개떡
        if(dto.getCoverImage() == null){
            coverImageFileType = "image/png";
        }
        else{
            coverImageFileType = dto.getCoverImage().getContentType();
        }
        this.title = dto.getTitle();
        this.coverImageUrl = "/"+seriesId+"/"+number+"/coverImage."+coverImageFileType.substring(coverImageFileType.lastIndexOf("/")+1);
        this.description = dto.getDescription();

    /*
        Set<Tag> tagSet = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .collect(Collectors.toSet());
        for (Tag tag : tagSet) {
            EpisodeTag episodeTag = EpisodeTag.builder()
                    .episode(this)
                    .tag(tag)
                    .build();
            this.tags.add(episodeTag);
        }
    */

        this.contents = dto.getContents();
        changeFilename();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class EpisodeKey implements Serializable {
        @Column(name = "series_id")
        private Long seriesId;
        @Column(name = "number")
        private Long number;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EpisodeKey key = (EpisodeKey) o;
            return Objects.equals(seriesId, key.seriesId) && Objects.equals(number, key.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(seriesId, number);
        }
    }
}
