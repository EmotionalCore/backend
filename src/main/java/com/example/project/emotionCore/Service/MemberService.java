package com.example.project.emotionCore.Service;


import com.example.project.emotionCore.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
  MemberDTO singIn(MemberDTO memberDTO);
  String singUp(MemberDTO memberDTO);
  String findPassword(MemberDTO memberDTO);
}
