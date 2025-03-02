package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Series;
import org.springframework.stereotype.Repository;


public interface CustomCommentRepository {
    void addLikeCount(Comment comment);
    void subLikeCount(Comment comment);
}
