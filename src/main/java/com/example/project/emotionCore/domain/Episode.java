package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "episode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(EpisodeId.class)
public class Episode {
//일단 figma에 있는것들 넣었는데 나중에 사라지면 지우기

    @Id
    @Column(name = "series_id", nullable = false)
    private int seriesId;

    @Id
    @GeneratedValue
    private Integer number;

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
}
