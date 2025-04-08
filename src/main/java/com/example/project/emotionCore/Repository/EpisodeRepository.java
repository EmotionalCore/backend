package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    Episode findBySeriesIdAndNumber(long seriesId, long number);
    List<Episode> findBySeriesId(long seriesId);
    void deleteBySeriesIdAndNumber(long seriesId, long number);
    Episode findTopBySeriesIdOrderByCreatedAtDesc(Long seriesId);
}
