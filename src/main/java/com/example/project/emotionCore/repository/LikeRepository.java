package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Like;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.domain.Series;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndSeriesId(Long memberId, Long seriesId);

    @Query("SELECT l FROM Like l WHERE l.member = :member AND l.series = :series AND l.comment.number = :number AND l.comment.commentId = :commentId")
    Optional<Like> findByMemberAndComment(
            @Param("member") Member member,
            @Param("series") Series series,
            @Param("number") Long number,
            @Param("commentId") Long commentId // commentId는 Long으로 처리
    );

    @Query("SELECT b.series FROM Like b WHERE b.member.id = :memberId")
    List<Series> findSeriesByMemberId(long memberId, Pageable pageable);
}

