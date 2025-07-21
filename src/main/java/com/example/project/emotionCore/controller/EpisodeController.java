package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "에피소드 API", description = "에피소드에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class EpisodeController {

    private final EpisodeService episodeService;

    @Operation(summary = "특정 series 회차(에피소드)들 가져오기")
    @GetMapping("/series")
    public ResponseEntity<PagedResponseDTO<EpisodePreviewDTO>> getAllEpisode(@RequestParam int index, @RequestParam int size, @RequestParam long seriesId) {
        List<EpisodePreviewDTO> episodePreviewDTO = episodeService.getEpisodeList(index,size,seriesId);
        int totalCount = episodeService.getEpisodeCount(seriesId);
        return ResponseEntity.ok(new PagedResponseDTO<>(episodePreviewDTO,totalCount));
    }

    //episode
    @Operation(summary = "episode 작성 기능")
    @PreAuthorize("hasRole('ADMIN') or @seriesService.isOwner(#episodeRequestDTO.seriesId, authentication.principal.id)")
    //ADMIN 혹은 자기글 이여야 작성 가능
    //? 권한 관리는 Service 영역이 깔끔한가?
    @PostMapping(value = "/episode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void writeEpisode(@ModelAttribute(value = "dto") EpisodeRequestDTO episodeRequestDTO){
        episodeService.saveNewEpisode(episodeRequestDTO);
    }

    //episode
    @Operation(summary = "episode 내용 가져오기")
    @GetMapping("/episode")
    public ResponseEntity<EpisodeResponseDTO> getEpisode(
            @RequestParam long seriesId,
            @RequestParam long number,
            Authentication authentication) {
        EpisodeResponseDTO episodeResponseDTO = episodeService.getEpisode(seriesId, number, authentication);
        return ResponseEntity.ok(episodeResponseDTO);
    }

    //episode
    @Operation(summary = "episode 삭제 기능")
    @DeleteMapping("/episode")
    @PreAuthorize("hasRole('ADMIN') or @seriesService.isOwner(#seriesId, authentication.principal.id)")
    public void deleteEpisode(@RequestParam long seriesId, @RequestParam long number) {
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        episodeService.deleteEpisode(seriesId, number);
    }

    //episode
    @Operation(summary = "episode 수정 기능")
    @PutMapping(value = "/episode", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @seriesService.isOwner(#episodeModifyDTO.seriesId, authentication.principal.id)")
    public void updateEpisode(@ModelAttribute EpisodeModifyDTO episodeModifyDTO) {
        //반환값 추가하든가 뭐 해야됨. 없는 에피소드 삭제해도 200 이던데
        episodeService.updateEpisode(episodeModifyDTO);
    }

}
