package com.example.project.emotionCore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeriesView {
    @Id
    @GeneratedValue()
    private int id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private int views;
}
