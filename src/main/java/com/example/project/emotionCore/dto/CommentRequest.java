package com.example.project.emotionCore.dto;

import com.example.project.emotionCore.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String content;
    private Member member;
}
