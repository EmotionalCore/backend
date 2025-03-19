package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Column(name = "tags")
    private String tags;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "view_count")
    private Long viewCount;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void renameFiles(Map<String, String> nameList){
        nameList.forEach((from, to) -> {
            contents = contents.replace(from, to);
        });
    }

    public Map<String, String> removeUuidFromContents(){ //코드 개떡
        String regex = "\\[\\*IMG&\\]\\(([^.]+)\\.([^)]+)\\)"; //[*IMG&](...)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contents);

        Map<String, String> nameList = new HashMap<>();
        int index = 0;
        while (matcher.find()) {
            String from = matcher.group(1)+"."+matcher.group(2);
            String to = index+"."+matcher.group(2);
            index++;
            contents = contents.replace("[*IMG&]("+from+")", "[*IMG&]("+to+")");
            nameList.put(from, to);
        }
        return nameList;
    }

    public String replaceName(String name, int index){
        String from = contents.substring(0, name.lastIndexOf('.'));
        return name.replace(from, String.valueOf(index));
    }

    public List<String> getFileNameFromContents(){
        String pattern = "\\[\\*IMG&]\\((.*?)\\)"; //[*IMG&](...)
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(contents);

        List<String> extracted = new ArrayList<>();
        while (matcher.find()) {
            extracted.add(matcher.group());
        }
        return extracted;
    }


    @AllArgsConstructor
    @NoArgsConstructor
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
