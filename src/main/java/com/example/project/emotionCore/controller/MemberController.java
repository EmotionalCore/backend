package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.dto.SigninCheckDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RestController("/api/member/")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("singup")    // 등록
    public String signUp(@RequestBody MemberDTO memberDTO){
        memberService.singUp(memberDTO);
        return "signup";
    }

    @PostMapping("signin/")  // 로그인 객체
    public SigninCheckDTO signIn(@RequestBody MemberDTO memberDTO, HttpServletRequest request) {
        MemberDTO memberDTO1 = memberService.singIn(memberDTO);
        HttpSession session = request.getSession();
        SigninCheckDTO signinCheckDTO = new SigninCheckDTO();

        if(memberDTO1 != null){ // 로그인 성공시 진행
            session.setAttribute("memberId", memberDTO1.getId());
            signinCheckDTO.setFlag(0);
            signinCheckDTO.setSessionId(session.getId());
        }
        else{
            signinCheckDTO.setFlag(1);
        }
        return signinCheckDTO;
    }

    @PostMapping("findpassword")   // 비밀번호 찾기
    public String findPassword(){
        return "findpassword";
    }



}
/*
1. 로그인
2. 비밀번호 찾기 (id, email) -> 이메일로 랜덤코드 발송
3. 회원가입                 -> 이메일로 인증코드 발송
*/