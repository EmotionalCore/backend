package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.dto.TagDTO;
import com.example.project.emotionCore.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "태그 API", description = "태그에 대한 CRUD 기능 담당")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "모든 태그 반환")
    @GetMapping("/tag/all")
    public ResponseEntity<List<TagDTO>> getAllTags(){
        List<TagDTO> tagDTO = tagService.getAllTags();
        return ResponseEntity.ok(tagDTO);
    }
}
