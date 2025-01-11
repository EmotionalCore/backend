package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.ErrorResponse;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.dto.SigninRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")    // 등록
    public String signUp(@RequestBody MemberDTO memberDTO){
        memberService.singUp(memberDTO);
        return "signup";
    }

    @PostMapping("/signin")  // 로그인
    public ResponseEntity<?> signIn(@RequestBody SigninRequestDTO signinRequestDTO) {
        try{
            JwtTokenDTO tokenDTO = memberService.singIn(signinRequestDTO.getEmail(), signinRequestDTO.getPassword());
            return ResponseEntity.ok(tokenDTO);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(401).body(new ErrorResponse(400,"Invalid Email or Password"));
        }
    }

    @PostMapping("/findpassword")   // 비밀번호 찾기
    public String findPassword() {
        return "findpassword";
    }
}
