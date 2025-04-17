package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.NaverCodeRequestDTO;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<JwtTokenDTO> naverSignIn(@RequestBody NaverCodeRequestDTO dto) {
        // MemberService를 호출하여 네이버 로그인 처리 및 JWT 생성
        JwtTokenDTO jwtToken = memberService.naverLogin(dto.getCode(),dto.getState());
        return ResponseEntity.ok(jwtToken); // 생성된 JWT 토큰 반환
    }
}
