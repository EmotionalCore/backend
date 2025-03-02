package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.BookMarkService;
import com.example.project.emotionCore.Service.LikeService;
import com.example.project.emotionCore.dto.BookMarkRequestDTO;
import com.example.project.emotionCore.dto.LikeRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "북마크 API", description = "북마크에 대한 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookMarkController {
    private final BookMarkService bookMarkService;

    @Operation(summary = "북마크 기능(일단 series에 관해서만)")
    @PostMapping()
    public ResponseEntity<Void> toggleBookMark(@RequestBody @Valid BookMarkRequestDTO bookMarkRequestDTO) {
        bookMarkService.toggleBookMark(bookMarkRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
