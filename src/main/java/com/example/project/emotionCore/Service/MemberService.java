package com.example.project.emotionCore.Service;


import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
  JwtTokenDTO singIn(String memberId, String password);
  String singUp(MemberDTO memberDTO);
  String findPassword(MemberDTO memberDTO);
}
