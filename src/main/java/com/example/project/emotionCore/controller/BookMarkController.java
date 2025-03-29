package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.BookMarkService;
import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.dto.BookMarkRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<Void> toggleBookMark(@AuthenticationPrincipal CustomMemberDetail customMemberDetail, @RequestBody @Valid BookMarkRequestDTO bookMarkRequestDTO) {
        long id = customMemberDetail.getId();
        bookMarkService.toggleBookMark(id, bookMarkRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
