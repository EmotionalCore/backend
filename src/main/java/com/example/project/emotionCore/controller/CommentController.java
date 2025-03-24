package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.Service.CommentService;
import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "댓글 API", description = "댓글에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @Operation(summary="댓글 생성")
    @PostMapping("/{seriesId}/{number}")
    public ResponseEntity<Comment> createComment(
            @AuthenticationPrincipal CustomMemberDetail customMemberDetail,
            @PathVariable Long seriesId,
            @PathVariable Long number,
            @RequestBody CommentRequest request
    ) {
        Member member = memberRepository.findById(customMemberDetail.getId())
                .orElseThrow(()->new RuntimeException("해당회원을 찾을 수 없음"));

        Comment saveComment= commentService.saveComment(seriesId,number, request.getContent(),member);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveComment);
    }

    @Operation(summary="댓글 모두 조회")
    @GetMapping("/{seriesId}/{number}")
    public ResponseEntity<List<Comment>> getCommentsByEpisode(
            @PathVariable Long number,
            @PathVariable Long seriesId
    )
        {
            List<Comment> comments = commentService.getCommentsByEpisode(number, seriesId);
            return ResponseEntity.ok(comments);

    }

    @Operation(summary="댓글 수정")
    @PreAuthorize("@commentService.isCommentOwner(#number, #seriesId, #commentId, authentication.principal.id)")
    @PostMapping("/{seriesId}/{number}/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @AuthenticationPrincipal CustomMemberDetail customMemberDetail,
            @PathVariable Long number,
            @PathVariable Long seriesId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ){
        Comment updateComment = commentService.updateComment(number, seriesId,commentId,request.getContent());
        return ResponseEntity.ok(updateComment);
    }

    @Operation(summary="댓글 삭제")
    @PreAuthorize("@commentService.isCommentOwner(#number, #seriesId, #commentId, authentication.principal.id)")
    @DeleteMapping("/{seriesId}/{number}/{commentId}")
    public ResponseEntity<Comment> deleteComment(
            @PathVariable Long number,
            @PathVariable Long seriesId,
            @PathVariable Long commentId
    ){
        commentService.deleteComment(seriesId,number,commentId);
        return ResponseEntity.noContent().build();
    }
}
