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

    @GetMapping()
    public ResponseEntity<Void> kakaoLogin(@RequestParam String code) {
        JwtTokenDTO jwtTokenDTO = kakaoService.socialLogin(code);
        String redirectUrl = "https://emotioncores.com/auth/oauth2/kakao"
                + "?accessToken=" + jwtTokenDTO.getAccessToken()
                + "&refreshToken=" +jwtTokenDTO.getRefreshToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
