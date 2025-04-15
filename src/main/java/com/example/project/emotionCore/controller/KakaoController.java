package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.KakaoService;
import com.example.project.emotionCore.dto.CodeRequestDTO;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JwtTokenDTO kakaoLogin(@RequestBody CodeRequestDTO dto) {
        return kakaoService.socialLogin(dto.getCode());
    }
}
