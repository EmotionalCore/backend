package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.service.GoogleService;
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
@RequestMapping(value = "/login/oauth2", produces = "application/json")
public class GoogleController {

    GoogleService googleService;

    @Autowired
    public GoogleController(GoogleService googleService) {
        this.googleService = googleService;
    }

    @PostMapping("/code/{registrationId}")
    public ResponseEntity<AccessTokenDTO> googleLogin(@RequestBody CodeRequestDTO dto) {
        JwtTokenDTO tokens = googleService.socialLogin(dto.getCode());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)    // 7일 유지
                .sameSite("Lax")
                .build();

        AccessTokenDTO googleAccessTokenDTO = new AccessTokenDTO(tokens.getAccessToken());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(googleAccessTokenDTO);
    }
}
