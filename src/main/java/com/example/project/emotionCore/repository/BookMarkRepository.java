package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.BookMark;
import com.example.project.emotionCore.domain.Series;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    Optional<BookMark> findByMemberIdAndSeriesId(Long memberId, Long seriesId);

    @Query("SELECT b.series FROM BookMark b WHERE b.member.id = :memberId")
    List<Series> findSeriesByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    int countByMemberId(Long memberId);
}
