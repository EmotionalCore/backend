package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import com.example.project.emotionCore.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    MemberService(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    public JwtTokenDTO singIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public String singUp(MemberDTO memberDTO) {
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberRepository.save(MemberMapper.toEntity(memberDTO));
        return "";
    }

    public String signUpWithSocial(String email, String username) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setUsername(username);
        memberDTO.setPassword("");  // 소셜 로그인이라 비밀번호는 필요 없음
        memberRepository.save(MemberMapper.toEntity(memberDTO));
        return "소셜 회원가입 완료";
    }

    // 이메일로 사용자 존재 여부 확인
    public boolean isEmailRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 소셜 로그인 시 기존 사용자 처리
    public JwtTokenDTO signInWithSocial(String email) {
        // 소셜 로그인은 비밀번호 없이 이메일만으로 로그인
        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(email, ""));
    }


    public String findPassword(MemberDTO memberDTO) {
        return "";
    }
}
