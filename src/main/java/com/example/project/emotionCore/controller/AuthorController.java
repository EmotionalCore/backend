package com.example.project.emotionCore.controller;


import com.example.project.emotionCore.dto.AuthorPreviewDTO;
import com.example.project.emotionCore.service.AuthorService;
import com.example.project.emotionCore.dto.NewAuthorDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/new")
    public ResponseEntity<List<NewAuthorDTO>> getNewAuthors(){
        List<NewAuthorDTO> newAuthors = authorService.getNewAuthors();
        return ResponseEntity.ok(newAuthors);
    }

    @Operation(summary = "이달의 우수 작가(작가가 등록한 작품들의 좋아요 기준)")
    @GetMapping("/best/monthly")
    public ResponseEntity<List<AuthorPreviewDTO>> getMonthlyBestAuthors() {
        int limit = 5;
        List<AuthorPreviewDTO> authorPreviewDTOS = authorService.getMonthlyBestAuthor(limit);
        return ResponseEntity.ok(authorPreviewDTOS);
    }

    //작가
    //작품을 1개 이상 올린 작가들에 한해서로 수정해야함
    @Operation(summary = "신규 작가들 반환")
    @GetMapping("/new/author")
    public ResponseEntity<List<AuthorPreviewDTO>> getNewAuthores() {
        int limit=6;
        List<AuthorPreviewDTO> AuthorPreviewDTOS = authorService.getNewAuthors(limit);
        return ResponseEntity.ok(AuthorPreviewDTOS);
    }
}
