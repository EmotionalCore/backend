package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
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
    public JwtTokenDTO signIn(@RequestBody MemberDTO memberDTO) {
        return memberService.singIn(memberDTO.getMemberId(), memberDTO.getPassword());
    }

    @PostMapping("/findpassword")   // 비밀번호 찾기
    public String findPassword() {
        return "findpassword";
    }
}
