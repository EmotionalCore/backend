package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(SeriesViewId.class)
@Table(name = "series_view")
public class SeriesView {

    @Id
    @Column(name = "series_id", nullable = false)
    private long seriesId;

    @Id
    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    @Column(nullable = false)
    @Builder.Default
    private Integer count = 0;

    public void incrementCount() {
        this.count++;
    }
}

