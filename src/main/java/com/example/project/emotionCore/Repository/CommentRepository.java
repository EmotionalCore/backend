package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, CommentId>, CustomCommentRepository {


    @Query("SELECT COALESCE(MAX(c.commentId), 0) + 1 FROM Comment c WHERE c.seriesId = :seriesId AND c.number = :number")
    Long getNextCommentId(@Param("seriesId") Long seriesId, @Param("number") Long number);

    List<Comment> findByNumberAndSeriesId(Long number, Long seriesId);

    Optional<Comment> findByNumberAndSeriesIdAndCommentId(Long number, Long seriesId, Long commentId);

    List<Comment> findByMember(Member member);

    Optional<Comment> findById(Long commentId);
}