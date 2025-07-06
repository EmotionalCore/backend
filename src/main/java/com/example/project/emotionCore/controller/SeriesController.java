package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.service.AuthorService;
import com.example.project.emotionCore.service.CustomMemberDetail;
import com.example.project.emotionCore.service.SearchWorkService;
import com.example.project.emotionCore.service.SeriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;

@Tag(name = "시리즈 API", description = "시리즈(시, 소설, 웹툰 등) 에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class SeriesController {
    private final SeriesService seriesService;
    private final AuthorService authorService;
    private final SearchWorkService searchWorkService;

    @Operation(summary = "오늘의 Best 작품들(조회수 기준)")
    @GetMapping("/best/today")
    public ResponseEntity<List<SeriesPreviewDTO>> getTodayBestSeries(@RequestParam(defaultValue = "5") int limit) {
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getTodayBestSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "추천 소설")
    @GetMapping("/recommend/novel")
    public ResponseEntity<List<NovelAndPoemPreviewDTO>> getRecommendedNovelSeries() {
        List<NovelAndPoemPreviewDTO> novelSeries = seriesService.getBestLikeNovelOrPoemSeries(WorkType.NOVEL);
        return ResponseEntity.ok(novelSeries);
    }

    @Operation(summary = "추천 시")
    @GetMapping("/recommend/poem")
    public ResponseEntity<List<NovelAndPoemPreviewDTO>> getRecommendedPoemSeries() {
        List<NovelAndPoemPreviewDTO> novelSeries = seriesService.getBestLikeNovelOrPoemSeries(WorkType.POEM);
        return ResponseEntity.ok(novelSeries);
    }

    @Operation(summary = "추천 웹툰")
    @GetMapping("/recommend/webtoon")
    public ResponseEntity<List<SeriesPreviewDTO>> getRecommendedWebtoonSeries() {
        List<SeriesPreviewDTO> webtoonSeries = seriesService.getBestLikeWebtoonSeries();
        return ResponseEntity.ok(webtoonSeries);
    }

    @Operation(summary = "이달의 인기 작품(조회수 기준)")
    @GetMapping("/popular/monthly")
    public ResponseEntity<List<SeriesPreviewDTO>> getMonthlyPopularSeries() {
        int limit = 5;
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getMonthlyBestSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "전체 작품 반환(최신순)")
    @GetMapping("/all")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeries(@RequestParam int index,@RequestParam int num) {
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getAllSeriesByCreatedDate(index, num);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "특정 타입의 작품 반환")
    @GetMapping("/type")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByType(@RequestParam int index,@RequestParam int num, @RequestParam String type) {
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getAllSeriesByType(index, num, type);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "(서재) 북마크 작품 반환")
    @GetMapping("/bookmark")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByBookMark(@RequestParam int index, @RequestParam int num, Authentication authentication) {
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getAllSeriesByBookMark(index, num, customMemberDetail.getId());
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "(서재) 좋아요 작품 반환")
    @GetMapping("/like")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByLike(@RequestParam int index, @RequestParam int num, Authentication authentication) {
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getAllSeriesByLike(index, num, customMemberDetail.getId());
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "특정 태그들을 모두 포함하는 작품 반환")
    @GetMapping("/tag")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByTag(@RequestParam List<String> tags) {
        List<SeriesPreviewDTO> series = seriesService.getAllSeriesByTag(tags);
        return ResponseEntity.ok(series);
    }

    @Operation(summary = "신규 작품들 반환")
    @GetMapping("/new")
    public ResponseEntity<List<SeriesPreviewDTO>> getNewSeries() {
        int limit=6;
        List<SeriesPreviewDTO> seriesPreviewDTOS = seriesService.getNewSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "특정 키워드의 검색 결과 반환 + 키워드 검색횟수 증가 반영")
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> getSeriesByKeywords(@RequestParam String keyword) {
        List<String> keywords = Arrays.stream(keyword.split(" ")).toList();
        List<SeriesDetailDTO> seriesDetailDTOS = seriesService.getSeriesByKeywords(keywords);
        List<AuthorDTO> authorDTOS = authorService.getAllByKeywords(keywords);

        searchWorkService.processSearch(keyword.trim());

        return ResponseEntity.ok(new SearchResponseDTO(seriesDetailDTOS, authorDTOS));
    }

    @Operation(summary = "series 작성 기능")
    @PreAuthorize("authentication.principal.id != Null")
    @PostMapping(value = "/series", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void writeSeries(@ModelAttribute SeriesRequestDTO seriesRequestDTO
            , Authentication authentication) {
        seriesService.saveNewSeries(seriesRequestDTO, authentication);
    }

    @Operation(summary = "특정 series 정보 가져오기")
    @GetMapping("/series/info")
    public ResponseEntity<SeriesDetailDTO> getSeriesInfo(@RequestParam long seriesId) {
        SeriesDetailDTO seriesDetailDTO = seriesService.getSeriesInfo(seriesId);
        return ResponseEntity.ok(seriesDetailDTO);
    }

    @Operation(summary = "series 삭제 기능")
    @DeleteMapping("/series")
    @PreAuthorize("hasRole('ADMIN') or @seriesService.isOwner(#seriesId, authentication.principal.id)")
    public void deleteSeries(@RequestParam long seriesId) {
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        seriesService.deleteSeries(seriesId);
    }

    @Operation(summary = "series 수정 기능")
    @PutMapping(value = "/series", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @seriesService.isOwner(#seriesModifyDTO.id, authentication.principal.id)")
    public void updateSeries(@ModelAttribute SeriesModifyDTO seriesModifyDTO){
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        seriesService.updateSeries(seriesModifyDTO);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) { //MultipartFile이 null일때 대체
        binder.registerCustomEditor(MultipartFile.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(null);
            }
        });
    }
}
