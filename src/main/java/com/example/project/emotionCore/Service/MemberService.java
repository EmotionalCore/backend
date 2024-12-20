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

    public String findPassword(MemberDTO memberDTO) {
        return "";
    }
}
