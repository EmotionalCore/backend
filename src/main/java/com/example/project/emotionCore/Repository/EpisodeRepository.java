package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer> {
    Episode findBySeriesIdAndNumber(long seriesId, long number);
    void deleteBySeriesIdAndNumber(long seriesId, long number);
}
