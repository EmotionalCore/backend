package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import com.example.project.emotionCore.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    MemberServiceImpl(MemberRepository memberRepository, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public JwtTokenDTO singIn(String memberId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public String singUp(MemberDTO memberDTO) {
        memberRepository.save(MemberMapper.toEntity(memberDTO));
        return "";
    }

    @Override
    public String findPassword(MemberDTO memberDTO) {
        return "";
    }
}
