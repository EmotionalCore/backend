package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.CommentRepository;
import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Member;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public boolean isCommentOwner(Long number,Long seriesId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findByNumberAndSeriesIdAndCommentId(number,seriesId,commentId)
                .orElseThrow(()->new EntityNotFoundException("Comment not found"));
        return comment.getMember().getId().equals(memberId);
    }

    //댓글 생성 C
    public Comment saveComment(Long seriesId, Long number, String content, Member member) {
        Long nextCommentId = commentRepository.getNextCommentId(seriesId, number);

        Comment newComment = Comment.builder()
                .seriesId(seriesId)
                .number(number)
                .commentId(nextCommentId) // 자동 증가된 댓글 ID 사용
                .commentContents(content)
                .commentDate(new Date())
                .commentLike(0)
                .member(member)
                .build();

        return commentRepository.save(newComment);
    }

    //모든 댓글 조회 R
    public List<Comment> getCommentsByEpisode(Long number, Long seriesId) {
        return commentRepository.findByNumberAndSeriesId( number, seriesId);
    }

    //업데이트 U
    public Comment updateComment(Long number, Long seriesId, Long commentId, String newContent) {
        Comment comment = commentRepository.findByNumberAndSeriesIdAndCommentId (number, seriesId,commentId)
                .orElseThrow(() -> new RuntimeException("수정할 댓글이 존재하지 않습니다."));
        comment.updateContent(newContent);
        return commentRepository.save(comment);
    }


    //댓글삭제 D
    public void deleteComment(Long number, Long seriesId, Long commentId) {
        Comment comment = commentRepository.findByNumberAndSeriesIdAndCommentId (number, seriesId,commentId)
                .orElseThrow(()->new RuntimeException("삭제할 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
