package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.CommentRepository;
import com.example.project.emotionCore.Repository.LikeRepository;
import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Like;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.LikeRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final SeriesRepository seriesRepository;
    private final CommentRepository commentRepository;  // 댓글 레포지토리 추가

    @Transactional
    public void toggleLike(Long id, LikeRequestDTO likeRequestDTO) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("멤버 id를 찾을 수 없습니다."));

        Like like = null;

        if (likeRequestDTO.getSeriesId() != null && likeRequestDTO.getCommentId() == null && likeRequestDTO.getNumber() == null) {
            // 시리즈에 대한 좋아요 처리
            Series series = seriesRepository.findById(likeRequestDTO.getSeriesId())
                    .orElseThrow(() -> new EntityNotFoundException("시리즈 id를 찾을 수 없습니다."));

            like = likeRepository.findByMemberIdAndSeriesId(id, (long) series.getId()).orElse(null);

            if (like != null) {
                likeRepository.delete(like);
                seriesRepository.subLikeCount(series);
            } else {
                likeRepository.save(Like.builder()
                        .series(series)
                        .member(member)
                        .build());
                seriesRepository.addLikeCount(series);
            }
        } else if (likeRequestDTO.getCommentId() != null && likeRequestDTO.getSeriesId() != null && likeRequestDTO.getNumber() != null) {
            // 댓글에 대한 좋아요 처리
            // 1. seriesId를 사용해 Series 객체를 조회
            Series series = seriesRepository.findById(likeRequestDTO.getSeriesId())
                    .orElseThrow(() -> new EntityNotFoundException("시리즈 id를 찾을 수 없습니다."));

            // 2. comment 조회
            Comment comment = commentRepository.findByNumberAndSeriesIdAndCommentId(
                            likeRequestDTO.getSeriesId(),
                            likeRequestDTO.getNumber(),
                            likeRequestDTO.getCommentId())
                    .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

            like = likeRepository.findByMemberAndComment(
                    member,
                    series,
                    likeRequestDTO.getNumber(),  // number 값을 넘겨줘야 함
                    likeRequestDTO.getCommentId() // commentId 값을 넘겨줘야 함
            ).orElse(null); // null로 처리

            if (like != null) {
                likeRepository.delete(like);
                commentRepository.subLikeCount(comment);
            } else {
                likeRepository.save(Like.builder()
                        .comment(comment)
                        .member(member)
                        .series(series)
                        .build());
                commentRepository.addLikeCount(comment);
            }
        }
    }
}

