package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.CommentService;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.dto.CommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API", description = "댓글에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary="댓글 생성")
    @PostMapping("/{seriesId}/{number}")
    public ResponseEntity<Comment> createComment(
            @PathVariable Long seriesId,
            @PathVariable Long number,
            @RequestBody CommentRequest request
    ) {
        Comment saveComment= commentService.saveComment(seriesId,number, request.getContent(), request.getMember());

        return ResponseEntity.status(HttpStatus.CREATED).body(saveComment);
    }

    @Operation(summary="댓글 모두 조회")
    @GetMapping("/{seriesId}/{number}")
    public ResponseEntity<List<Comment>> getCommentsByEpisode(
            @PathVariable Long seriesId,
            @PathVariable Long number
    )
        {
            List<Comment> comments = commentService.getCommentsByEpisode(seriesId,number);
            return ResponseEntity.ok(comments);

    }
    @Operation(summary="댓글 수정")
    @PostMapping("/{seriesId}/{number}/{commentId}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable Long seriesId,
            @PathVariable Long number,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request
    ){
        Comment updateComment = commentService.updateComment(seriesId,number,commentId,request.getContent());
        return ResponseEntity.ok(updateComment);
    }

    @Operation(summary="댓글 삭제")
    @DeleteMapping("/{seriesId}/{number}/{commentId}")
    public ResponseEntity<Comment> deleteComment(
            @PathVariable Long seriesId,
            @PathVariable Long number,
            @PathVariable Long commentId
    ){
        commentService.deleteComment(seriesId,number,commentId);
        return ResponseEntity.noContent().build();
    }

}
