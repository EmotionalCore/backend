package com.example.project.emotionCore.module.mapper;

import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.MemberDTO;

public class MemberMapper {
    public static Member toEntity(MemberDTO memberDTO){
        return Member.builder()
                .id(memberDTO.getId())
                .email(memberDTO.getEmail())
                .username(memberDTO.getUsername())
                .password(memberDTO.getPassword())
                .build();
    }

    public static MemberDTO toDto(Member member){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setUsername(member.getUsername());
        memberDTO.setPassword(member.getPassword());
        return memberDTO;
    }
}
