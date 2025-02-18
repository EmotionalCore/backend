package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.domain.SeriesView;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface SeriesViewRepository extends CrudRepository<SeriesView, Integer> {
    Optional<SeriesView> findBySeriesIdAndViewDate(long seriesId, LocalDate viewDate);
}
