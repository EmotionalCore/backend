package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.WorkViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkViewLogRepository extends JpaRepository<WorkViewLog, Long> {
    Optional<WorkViewLog> findBySeriesIdAndEpisodeNumberAndMemberId(long seriesId, long episodeNumber, long memberId);
}
