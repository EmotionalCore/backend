package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface CustomSeriesRepository {
    List<Series> findNDaysTopViewSeries(LocalDate startDate, LocalDate endDate, int limit);
    List<Series> findByKeywords(List<String> keyword);
}
