package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.KakaoService;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth/oauth2/kakao", produces = "application/json")
public class KakaoController {

    KakaoService kakaoService;

    @Autowired
    public KakaoController(KakaoService kakaoService) {
        this.kakaoService = kakaoService;
    }

    @PostMapping()
    public JwtTokenDTO kakaoLogin(@RequestParam String code) {
        return kakaoService.socialLogin(code);
    }
}
