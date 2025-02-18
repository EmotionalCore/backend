package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.AuthorService;
import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.Service.CustomUserDetailService;
import com.example.project.emotionCore.Service.SearchWorkService;
import com.example.project.emotionCore.Service.SearchWorkService;
import com.example.project.emotionCore.Service.WorkService;
import com.example.project.emotionCore.config.SecurityConfig;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import com.nimbusds.jose.proc.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "작품 API", description = "작품(시, 소설, 웹툰 등) 에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class WorkController {
    WorkService workService;
    SearchWorkService searchWorkService;
    private final AuthorService authorService;

    @Autowired
    WorkController(WorkService workService, AuthorService authorService) {
        this.workService = workService;
        this.authorService = authorService;
    }

    @Operation(summary = "(작업 완료) 오늘의 Best 작품들(조회수 기준)")
    @GetMapping("/best/today")
    public ResponseEntity<List<SeriesPreviewDTO>> getTodayBestSeries(@RequestParam(defaultValue = "5") int limit) {
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

    @Operation(summary = "(작업 완료) 이달의 인기 작품(조회수 기준)")
    @GetMapping("/popular/monthly")
    public ResponseEntity<List<SeriesPreviewDTO>> getMonthlyPopularSeries() {
        int limit = 3;
        List<SeriesPreviewDTO> seriesPreviewDTOS = workService.getMonthlyBestSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "(작업완료) 이달의 우수 작가(작가가 등록한 작품들의 좋아요 기준)")
    @GetMapping("/author/best/monthly")
    public ResponseEntity<List<AuthorPreviewDTO>> getMonthlyBestAuthors() {
        int limit = 3;
        List<AuthorPreviewDTO> authorPreviewDTOS = workService.getMonthlyBestAuthor(limit);
        return ResponseEntity.ok(authorPreviewDTOS);
    }

    @Operation(summary = "(작업 완료) 전체 작품 반환(최신순)")
    @GetMapping("/all")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeries(@RequestParam int index,@RequestParam int num) {
        List<SeriesPreviewDTO> seriesPreviewDTOS = workService.getAllSeriesByCreatedDate(index, num);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    @Operation(summary = "(작업 완료) 특정 타입의 작품 반환")
    @GetMapping("/type")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByType(@RequestParam int index,@RequestParam int num, @RequestParam String type) {
        List<SeriesPreviewDTO> seriesPreviewDTOS = workService.getAllSeriesByType(index, num, type);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }


    /**
    *승헌 시작
     */


    @Operation(summary = "(작업 완료) 특정 태그들을 모두 포함하는 작품 반환")
    @GetMapping("/tag")
    public ResponseEntity<List<SeriesPreviewDTO>> getAllSeriesByTag(@RequestParam List<String> tags) {
        List<SeriesPreviewDTO> series = workService.getAllSeriesByTag(tags);
        return ResponseEntity.ok(series);
    }

    @Operation(summary = "(작업 완료) 인기 검색어들 반환")
    @GetMapping("/search/popular")
    public ResponseEntity<List<SearchWorkDTO>> getPopularSearchKeywords() {
        // 서비스 호출
        List<SearchWorkDTO> popularSearches = workService.getBestSearchWork();
        return ResponseEntity.ok(popularSearches);
    }

    @Operation(summary = "(작업 완료) 신규 작품들 반환")
    @GetMapping("/new")
    public ResponseEntity<List<SeriesPreviewDTO>> getNewSeries() {
        int limit=6;
        List<SeriesPreviewDTO> seriesPreviewDTOS = workService.getNewSeries(limit);
        return ResponseEntity.ok(seriesPreviewDTOS);
    }

    //작품을 1개 이상 올린 작가들에 한해서로 수정해야함
    @Operation(summary = "(작업완료) 신규 작가들 반환")
    @GetMapping("/new/author")
    public ResponseEntity<List<AuthorPreviewDTO>> getNewAuthors() {
        int limit=6;
        List<AuthorPreviewDTO> AuthorPreviewDTOS = workService.getNewAuthors(limit);
        return ResponseEntity.ok(AuthorPreviewDTOS);
    }

    /*
    건아 시작
     */

    @Operation(summary = "(작업 완료) 특정 키워드의 검색 결과 반환")
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> getSeriesByKeywords(@P("Horror Comedy") String keyword) {
        List<String> keywords = Arrays.stream(keyword.split(" ")).toList();
        List<SeriesDetailDTO> seriesDetailDTOS = workService.getSeriesByKeywords(keywords);
        List<AuthorDTO> authorDTOS = authorService.getAllByKeywords(keywords);
        return ResponseEntity.ok(new SearchResponseDTO(seriesDetailDTOS, authorDTOS));
    }

    @Operation(summary = "자기가 올린 series 리스트 가져오기")
    @PreAuthorize("authentication.principal.id != Null")
    @GetMapping("/mywork")
    public ResponseEntity<List<SeriesIdAndNameDTO>> getMyWorkSeries(Authentication authentication) {
        List<SeriesIdAndNameDTO> dto = workService.getMySeries(authentication);
        return ResponseEntity.ok(dto);
    }



    @Operation(summary = "(작업 중) episode 작성 기능")
    @PreAuthorize("hasRole('ADMIN') or @workService.isOwner(#episodeRequestDTO.seriesId, authentication.principal.id)")
    //ADMIN 혹은 자기글 이여야 작성 가능
    //? 권한 관리는 Service 영역이 깔끔한가?
    @PostMapping("/episode")
    public void getSeriesByEpisode(@RequestBody EpisodeRequestDTO episodeRequestDTO) {
        workService.saveNewEpisode(episodeRequestDTO);
    }

    @Operation(summary = "episode 내용 가져오기")
    @GetMapping("/episode")
    public ResponseEntity<EpisodeResponseDTO> getSeriesByEpisode(
                                                    @RequestParam long seriesId,
                                                    @RequestParam long number,
                                                    Authentication authentication) {
        EpisodeResponseDTO episodeResponseDTO = workService.getEpisode(seriesId, number, authentication);
        return ResponseEntity.ok(episodeResponseDTO);
    }

    @Operation(summary = "episode 삭제 기능")
    @DeleteMapping("/episode")
    @PreAuthorize("hasRole('ADMIN') or @workService.isOwner(#seriesId, authentication.principal.id)")
    public void deleteEpisode(@RequestParam long seriesId, @RequestParam long number) {
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        workService.deleteEpisode(seriesId, number);
    }

    @Operation(summary = "episode 수정 기능")
    @PutMapping("/episode")
    @PreAuthorize("hasRole('ADMIN') or @workService.isOwner(#episodeModifyDTO.seriesId, authentication.principal.id)")
    public void updateEpisode(@RequestBody EpisodeModifyDTO episodeModifyDTO) {
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        workService.updateEpisode(episodeModifyDTO);
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
    public ResponseEntity<String> getTest(Authentication authentication) {
        CustomMemberDetail c = (CustomMemberDetail) authentication.getPrincipal();
        return ResponseEntity.ok("Hi " + c.getUsername());
    }

}
