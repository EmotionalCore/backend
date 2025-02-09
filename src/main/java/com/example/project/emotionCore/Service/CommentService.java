package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.CommentRepository;
import com.example.project.emotionCore.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;

    @Transactional
    public void increaseLike(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        comment.increaseLike();
    }



}
