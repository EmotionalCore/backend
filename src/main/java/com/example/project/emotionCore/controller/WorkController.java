package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.WorkService;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "작품 API", description = "작품(시, 소설, 웹툰 등) 에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class WorkController {
    WorkService workService;

    @Autowired
    WorkController(WorkService workService) {
        this.workService = workService;
    }

    @Operation(summary = "(작업 완료) 오늘의 Best 작품들(조회수 기준)")
    @GetMapping("/best/today")
    public ResponseEntity<List<SeriesPreviewDTO>> getTodayBestSeries() {
        int limit = 4;
        List<SeriesPreviewDTO> seriesPreviewDTOS = workService.getTodayBestSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "(작업 완료) 추천 소설")
    @GetMapping("/recommend/novel")
    public ResponseEntity<List<NovelAndPoemPreviewDTO>> getRecommendedNovelSeries() {
        List<NovelAndPoemPreviewDTO> novelSeries = workService.getBestLikeNovelOrPoemSeries(WorkType.NOVEL);
        return ResponseEntity.ok(novelSeries);
    }

    @Operation(summary = "(작업 완료) 추천 시")
    @GetMapping("/recommend/poem")
    public ResponseEntity<List<NovelAndPoemPreviewDTO>> getRecommendedPoemSeries() {
        List<NovelAndPoemPreviewDTO> novelSeries = workService.getBestLikeNovelOrPoemSeries(WorkType.POEM);
        return ResponseEntity.ok(novelSeries);
    }

    @Operation(summary = "(작업 완료) 추천 웹툰")
    @GetMapping("/recommend/webtoon")
    public ResponseEntity<List<SeriesPreviewDTO>> getRecommendedWebtoonSeries() {
        List<SeriesPreviewDTO> webtoonSeries = workService.getBestLikeWebtoonSeries();
        return ResponseEntity.ok(webtoonSeries);
    }

    @Operation(summary = "(작업중) 이달의 인기 작품")
    @GetMapping("/popular/monthly")
    public ResponseEntity<List<SeriesPreviewDTO>> getMonthlyPopularSeries() {
        return null;
    }

    @Operation(summary = "(작업중) 이달의 우수 작가")
    @GetMapping("/author/best/monthly")
    public ResponseEntity<List<AuthorPreviewDTO>> getMonthlyBestAuthors() {
        return null;
    }

    @Operation(summary = "(작업중) 전체 작품 반환")
    @GetMapping("/all")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeries(@RequestParam int num) {
        return null;
    }

    @Operation(summary = "(작업중) 특정 타입의 작품 반환")
    @GetMapping("/type")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByType(@RequestParam String type) {
        return null;
    }

    @Operation(summary = "(작업중) 특정 태그들을 포함하는 작품 반환")
    @GetMapping("/tag")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByTag(@RequestParam List<String> tags) {
        return null;
    }

    @Operation(summary = "(작업중) 인기 검색어들 반환")
    @GetMapping("/search/popular")
    public ResponseEntity<List<String>> getPopularSearchKeywords() {
        return null;
    }

    @Operation(summary = "(작업중) 신규 작품들 반환")
    @GetMapping("/new")
    public ResponseEntity<List<SeriesPreviewDTO>> getNewSeries() {
        return null;
    }

    @Operation(summary = "(작업중) 신규 작가들 반환")
    @GetMapping("/new/author")
    public ResponseEntity<List<AuthorPreviewDTO>> getNewAuthor() {
        return null;
    }

    @Operation(summary = "(작업중) 특정 키워드의 검색 결과 반환")
    @GetMapping("/search")
    public ResponseEntity<List<SeriesDetailDTO>> getSeriesByKeyword(@P("keyword") String keyword) {
        return null;
    }














    @Operation(
            summary = "테스트 용도 입니다.",
            description = "테스트임."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 조회됨"),
            @ApiResponse(responseCode = "404", description = "작품을 찾을 수 없음", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/aTest")
    public ResponseEntity<SuccessResponse<List<SeriesPreviewDTO>>> getTest(@RequestParam int num) {
        if(num == 1){
            return ResponseEntity.ok(null);
        }
        else{
            throw new CustomBadRequestException(400, "잘못된 요청입니다.");
        }
    }

}
