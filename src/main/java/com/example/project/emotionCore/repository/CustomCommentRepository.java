package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Comment;


public interface CustomCommentRepository {
    void addLikeCount(Comment comment);
    void subLikeCount(Comment comment);
}
