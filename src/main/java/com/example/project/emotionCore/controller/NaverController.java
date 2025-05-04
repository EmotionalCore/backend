package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.NaverCodeRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class NaverController {

    private final MemberService memberService;

    /**
     * 네이버 로그인 엔드포인트
     *
     * code  네이버에서 제공한 인증 코드
     * state 네이버 로그인 요청의 상태값
     * @return JwtTokenDTO (AccessToken, RefreshToken 포함)
     */
    @PostMapping("/naver")
    public ResponseEntity<String> naverSignIn(@RequestBody NaverCodeRequestDTO dto) {
        JwtTokenDTO tokens = memberService.naverLogin(dto.getCode(), dto.getState());

        // Refresh Token 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)    // 7일 유지
                .sameSite("Lax")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(tokens.getAccessToken());
    }
}
