package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.WorkService;
import com.example.project.emotionCore.dto.AuthorPreviewDTO;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.enums.WorkType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class WorkController {
    WorkService workService = new WorkService();

    @GetMapping("/best/today")
    public ResponseEntity<List<SeriesPreviewDTO>> getTodayBestSeries() {
        return null;
    }

    @GetMapping("/recommend")
    public ResponseEntity<?> getRecommendedSeries(@RequestParam String type) {
        WorkType workType;
        try {
            workType = WorkType.fromString(type);  // 문자열을 enum으로 변환
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid work type: " + type);
        }

        List<?> previewDTOs = null;
        switch (workType) {
            case NOVEL:
                // 소설 추천 처리
                break;
            case POEM:
                // 시 추천 처리
                break;
            case WEBTOON:
                // 웹툰 추천 처리
                break;
            default:
                return ResponseEntity.badRequest().body("Unsupported work type.");
        }

        return ResponseEntity.ok(previewDTOs);
    }

    @GetMapping("/popular/monthly")
    public ResponseEntity<List<SeriesPreviewDTO>> getMonthlyPopularSeries() {
        return null;
    }

    @GetMapping("/author/best/monthly")
    public ResponseEntity<List<AuthorPreviewDTO>> getMonthlyBestAuthors() {
        return null;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeries(@RequestParam int num) {
        return null;
    }

    @GetMapping("/type")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByType(@RequestParam String type) {
        return null;
    }

    @GetMapping("/tag")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByTag(@RequestParam List<String> tags) {
        return null;
    }

    @GetMapping("/search/popular")
    public ResponseEntity<List<String>> getPopularSearchKeywords() {
        return null;
    }

    @GetMapping("/new")
    public ResponseEntity<List<SeriesPreviewDTO>> getNewSeries() {
        return null;
    }

    @GetMapping("/new/author")
    public ResponseEntity<List<AuthorPreviewDTO>> getNewAuthor() {
        return null;
    }

    @GetMapping("/search")
    public ResponseEntity<List<SeriesPreviewDTO>> getSeriesByKeyword(@P("keyword") String keyword) {
        return null;
    }


}
