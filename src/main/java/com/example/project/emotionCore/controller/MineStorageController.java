package com.example.project.emotionCore.controller;


import com.example.project.emotionCore.dto.PagedResponseDTO;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.service.CustomMemberDetail;
import com.example.project.emotionCore.service.MineStorageService;
import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="서재 API", description = "서재댓글부분 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storage")
public class MineStorageController {

    private final MineStorageService mineStorageService;
    private final MemberRepository memberRepository;

    @Operation(summary = "(서재) 북마크 작품 반환")
    @GetMapping("/bookmark")
    public ResponseEntity<PagedResponseDTO<SeriesPreviewDTO>> getAllSeriesByBookMark(@RequestParam int index, @RequestParam int num, Authentication authentication) {
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        Long memberId = customMemberDetail.getId();
        List<SeriesPreviewDTO> seriesPreviewDTOS = mineStorageService.getAllSeriesByBookMark(index, num, memberId);
        int totalCount = mineStorageService.getBookmarkedSeriesCount(memberId);
        return ResponseEntity.ok(new PagedResponseDTO<>(seriesPreviewDTOS, totalCount));
    }

    @Operation(summary = "(서재) 좋아요 작품 반환")
    @GetMapping("/like")
    public ResponseEntity<PagedResponseDTO<SeriesPreviewDTO>> getAllSeriesByLike(@RequestParam int index, @RequestParam int num, Authentication authentication) {
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        Long memberId = customMemberDetail.getId();
        List<SeriesPreviewDTO> seriesPreviewDTOS = mineStorageService.getAllSeriesByLike(index, num, memberId);
        int totalCount = mineStorageService.getLikedSeriesCount(memberId);
        return ResponseEntity.ok(new PagedResponseDTO<>(seriesPreviewDTOS, totalCount));
    }


    @Operation(summary="내가 작석한 댓글의 작품 조회")
    @GetMapping("/comment")
    public ResponseEntity<List<Episode>> getMyEpisodes(@AuthenticationPrincipal CustomMemberDetail customMemberDetail) {


        long id=customMemberDetail.getId();
        Member member = memberRepository.findById(id)
                .orElseThrow(()->new RuntimeException("해당 회원을 찾을 수 없습니다."));

        List<Episode> episodes = mineStorageService.getEpisodesByMember(member);

        return ResponseEntity.ok(episodes);
    }
}
