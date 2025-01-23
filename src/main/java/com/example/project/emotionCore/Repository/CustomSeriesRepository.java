package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;

import java.time.LocalDate;
import java.util.List;

public interface CustomSeriesRepository {
    List<Series> findTodayBestSeries(int days, int limit);
    List<Series> findAllByTagsContaining(List<String> tags);
}
