package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.KakaoService;
import com.example.project.emotionCore.dto.AccessTokenDTO;
import com.example.project.emotionCore.dto.CodeRequestDTO;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AccessTokenDTO> kakaoLogin(@RequestBody CodeRequestDTO dto) {
        JwtTokenDTO tokens = kakaoService.socialLogin(dto.getCode());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)    // 7일 유지
                .sameSite("Lax")
                .build();

        AccessTokenDTO kakaoAccessTokenDTO = new AccessTokenDTO(tokens.getAccessToken());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(kakaoAccessTokenDTO);
    }
}
