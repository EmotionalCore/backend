package com.example.project.emotionCore.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private Author authorInfos;

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

}
