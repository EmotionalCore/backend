package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.dto.MemberDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository { //DB와 연결하는 역할
    public void addMember();
    public void findPassword();
    public MemberDTO getMember();
}
