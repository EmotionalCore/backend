package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findById(long id);
}
