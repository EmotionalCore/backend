package com.example.project.emotionCore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class EpisodeId implements Serializable {
    @Column(name = "series_id", nullable = false)
    private int seriesId;
    @Column(name = "number", nullable = false)
    private int number;
}
