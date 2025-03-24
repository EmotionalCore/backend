package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.Service.LikeService;
import com.example.project.emotionCore.dto.LikeRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "좋아요 API", description = "좋아요에 대한 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 기능(일단 series에 관해서만)")
    @PostMapping()
    public ResponseEntity<Void> toggleLike(@AuthenticationPrincipal CustomMemberDetail customMemberDetail, @RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        Long id = customMemberDetail.getId();
        likeService.toggleLike(id, likeRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
