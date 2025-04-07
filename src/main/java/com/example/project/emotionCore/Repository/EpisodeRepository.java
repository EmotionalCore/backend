package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.dto.NewAuthorDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("""
    SELECT new com.example.project.emotionCore.dto.NewAuthorDTO(
        s.authorId,
        m.username,
        m.profileImageUrl,
        COUNT(e),
        MAX(e.createdAt)
    )
    FROM Episode e
    JOIN Series s ON e.seriesId = s.id
    JOIN Member m ON s.authorId = m.id
    GROUP BY s.authorId, m.username, m.profileImageUrl
    HAVING COUNT(e) <= 2
    ORDER BY MAX(e.createdAt) DESC
""")
    List<NewAuthorDTO> findNewAuthors(Pageable pageable);

}
