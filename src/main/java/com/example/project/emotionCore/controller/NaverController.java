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
    public ResponseEntity<Void> naverSignIn(@RequestBody NaverCodeRequestDTO dto) {
        JwtTokenDTO tokens = memberService.naverLogin(dto.getCode(), dto.getState());

        // Access Token 쿠키 생성
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", tokens.getAccessToken())
                .httpOnly(true)    // 클라이언트 JavaScript에서 접근 불가
                .secure(true)      // HTTPS 통신에서만 전송
                .path("/")         // 쿠키 경로
                .maxAge(3600)      // 1시간 유지
                .sameSite("Strict")
                .build();

        // Refresh Token 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)    // 7일 유지
                .sameSite("Strict")
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }
}
