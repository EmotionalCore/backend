package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@Data
public class SeriesViewId implements Serializable {
    @Column(name = "series_id", nullable = false)
    private long seriesId;
    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;
}
