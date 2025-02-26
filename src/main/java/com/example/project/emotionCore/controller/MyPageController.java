package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.CustomMemberDetail;
import com.example.project.emotionCore.Service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private MemberService memberService;

    @Operation(summary="회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal CustomMemberDetail customMemberDetail) {
        Long id = customMemberDetail.getMemberId();
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원탈퇴가 완료 되었습니다.");
    }
}
