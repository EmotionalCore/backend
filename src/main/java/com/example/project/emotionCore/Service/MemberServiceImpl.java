package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.module.mapper.MemberMapper;
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
        Member member = memberRepository.findByMemberIdAndPassword(memberDTO.getMemberId(), memberDTO.getPassword());
        if(member != null){
            return MemberMapper.toDto(member);
        }
        return null;
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
