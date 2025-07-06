package com.example.project.emotionCore.controller;


import com.example.project.emotionCore.dto.SearchWorkDTO;
import com.example.project.emotionCore.service.SearchWorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "작품 API", description = "작품(시, 소설, 웹툰 등) 에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class SearchWorkController {

    private final SearchWorkService searchWorkService;

    //search
    @Operation(summary = "인기 검색어들 반환")
    @GetMapping("/search/popular")
    public ResponseEntity<List<SearchWorkDTO>> getPopularSearchKeywords() {
        // 서비스 호출
        List<SearchWorkDTO> popularSearches = searchWorkService.getBestSearchWork();
        return ResponseEntity.ok(popularSearches);
    }
}
