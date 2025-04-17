package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.GoogleService;
import com.example.project.emotionCore.dto.CodeRequestDTO;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class GoogleController {

    GoogleService googleService;

    @Autowired
    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @PostMapping("/code/{registrationId}")
    public JwtTokenDTO googleLogin(@RequestBody CodeRequestDTO dto) {
        return googleService.socialLogin(dto.getCode());
    }
}
