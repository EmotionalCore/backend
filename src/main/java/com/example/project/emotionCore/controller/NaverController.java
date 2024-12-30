package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
@RequiredArgsConstructor
public class NaverController {

    private final MemberService memberService;

    /**
     * 네이버 로그인 엔드포인트
     *
     * @param code  네이버에서 제공한 인증 코드
     * @param state 네이버 로그인 요청의 상태값
     * @return JwtTokenDTO (AccessToken, RefreshToken 포함)
     */
    @GetMapping("/naver")
    public ResponseEntity<JwtTokenDTO> naverSignIn(@RequestParam String code, @RequestParam String state) {
        // MemberService를 호출하여 네이버 로그인 처리 및 JWT 생성
        JwtTokenDTO jwtToken = memberService.naverLogin(code, state);
        return ResponseEntity.ok(jwtToken); // 생성된 JWT 토큰 반환
    }
}
