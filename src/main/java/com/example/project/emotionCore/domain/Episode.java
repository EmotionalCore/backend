package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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

    @AllArgsConstructor
    @NoArgsConstructor
    public static class EpisodeKey implements Serializable {
        private Long seriesId;
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
