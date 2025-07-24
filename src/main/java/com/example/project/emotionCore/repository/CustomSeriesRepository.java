package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.SeriesViewedPreviewDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomSeriesRepository {
    List<Series> findNDaysTopViewSeries(LocalDate startDate, LocalDate endDate, int limit);
    List<Series> findByKeywords(List<String> keyword);
    List<Series> findTodayBestSeries(int days, int limit);
    List<Series> findAllByTagsContaining(List<String> tags);
    List<Series> findAllByTypeAndTags(Pageable pageable, String type, List<String> tags);
    List<Series> findMonthlyBestSeries(int limit);
    void addLikeCount(Series Series);
    void subLikeCount(Series Series);
    void addBookMarkCount(Series Series);
    void subBookMarkCount(Series Series);
    List<SeriesViewedPreviewDTO> findViewListByMemberId(Pageable pageable,long memberId);
    int countByTypeAndTags(String type, List<String> tags);
    int countByTags(List<String> tags);
}
