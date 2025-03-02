package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.SeriesViewedPreviewDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomSeriesRepository {
    List<Series> findNDaysTopViewSeries(LocalDate startDate, LocalDate endDate, int limit);
    List<Series> findByKeywords(List<String> keyword);
    List<Series> findTodayBestSeries(int days, int limit);
    List<Series> findAllByTagsContaining(List<String> tags);
    List<Series> findMonthlyBestSeries(int limit);
    void addLikeCount(Series Series);
    void subLikeCount(Series Series);
    void addBookMarkCount(Series Series);
    void subBookMarkCount(Series Series);
    List<SeriesViewedPreviewDTO> findViewListByMemberId(long memberId);
}
