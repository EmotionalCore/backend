package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.CommentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, CommentId> {

    @Query("SELECT COALESCE(MAX(c.commentId), 0) + 1 FROM Comment c WHERE c.seriesId = :seriesId AND c.number = :number")
    Long getNextCommentId(@Param("seriesId") Long seriesId, @Param("number") Long number);

    List<Comment> findByEpisodeNumberAndSeriesId(Long number,Long seriesId);
    Optional<Comment> findByEpisodeNumberAndSeriesIdAndCommentId(Long number,Long seriesId,Long commentId);
}