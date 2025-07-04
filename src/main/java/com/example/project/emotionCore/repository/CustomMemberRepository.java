package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.dto.MemberDetailDTO;

public interface CustomMemberRepository {
    MemberDetailDTO findMemberDetailById(Long id);
}
