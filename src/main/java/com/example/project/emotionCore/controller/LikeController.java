package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.LikeService;
import com.example.project.emotionCore.dto.LikeRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "좋아요 API", description = "좋아요에 대한 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "좋아요 기능(일단 series에 관해서만)")
    @PostMapping()
    public ResponseEntity<Void> toggleLike(@RequestBody @Valid LikeRequestDTO likeRequestDTO) {
        likeService.toggleLike(likeRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
