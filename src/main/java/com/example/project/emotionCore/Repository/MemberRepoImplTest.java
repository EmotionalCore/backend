package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.dto.MemberDTO;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepoImplTest implements MemberRepository{
    @Override
    public void addMember() {
        return;
    }

    @Override
    public void findPassword() {
        return;
    }

    @Override
    public MemberDTO getMember() {
        return null;
    }
}
