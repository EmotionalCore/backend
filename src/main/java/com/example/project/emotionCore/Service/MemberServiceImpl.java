package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.dto.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Autowired
    MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    MemberRepository getMemberRepository(MemberRepository memberRepository) {
        return memberRepository;
    }

    @Override
    public MemberDTO singIn(MemberDTO memberDTO) {
        return memberRepository.getMember();
    }

    @Override
    public String singUp(MemberDTO memberDTO) {
        memberRepository.addMember();
        return "";
    }

    @Override
    public String findPassword(MemberDTO memberDTO) {
        return "";
    }
}
