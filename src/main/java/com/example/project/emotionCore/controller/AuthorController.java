package com.example.project.emotionCore.controller;


import com.example.project.emotionCore.Service.AuthorService;
import com.example.project.emotionCore.dto.NewAuthorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/new")
    public ResponseEntity<List<NewAuthorDTO>> getNewAuthors(){
        List<NewAuthorDTO> newAuthors = authorService.getNewAuthors();
        return ResponseEntity.ok(newAuthors);
    }
}
