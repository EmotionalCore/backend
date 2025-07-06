package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.repository.AuthorRepository;
import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.service.AuthorService;
import com.example.project.emotionCore.service.CustomMemberDetail;
import com.example.project.emotionCore.service.MemberService;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.MyPageUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final AuthorService authorService;
    private final MemberRepository memberRepository;
    private final AuthorRepository authorRepository;

    @Operation(summary="회원 탈퇴")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMember(@AuthenticationPrincipal CustomMemberDetail customMemberDetail) {
        if (customMemberDetail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증이 필요합니다.");
        }
        final Long id = customMemberDetail.getId();
        memberService.deleteMember(id);
        return ResponseEntity.ok("회원탈퇴가 완료 되었습니다.");
    }

    @Operation(summary="마이페이지 회원 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<String> updateMyPage(
            @AuthenticationPrincipal CustomMemberDetail customMemberDetail,
            @RequestBody MyPageUpdateDTO mypageupdateDTO
    ) {
            long id = customMemberDetail.getId();
            memberService.updateMember(id, mypageupdateDTO);
            authorService.updateAuthor(id, mypageupdateDTO);
        return ResponseEntity.ok("회원정보가 업데이트 되었습니다.");
    }


    @Operation(summary="본인Detail 가져오기")
    @GetMapping("/mydetail")
    public ResponseEntity<MyPageUpdateDTO> getMyId(@AuthenticationPrincipal CustomMemberDetail customMemberDetail) {
        if(customMemberDetail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(null);  // 혹은 .build()로 빈 바디 반환
        }
        Long memberId = customMemberDetail.getId();
        Member member = memberRepository.findById(memberId).orElse(null);
        Author author = authorRepository.findById(memberId);

        // DTO에 값 세팅
        MyPageUpdateDTO dto = new MyPageUpdateDTO();
        dto.setUsername(member.getUsername());
        dto.setEmail(member.getEmail());
        dto.setProfileImageUrl(member.getProfileImageUrl());

        if(author != null){
            dto.setDescription(author.getDescription());
            dto.setLinks(author.getLinks());
            // tags 가 Set<Tag> 형태라면, 이름(String)만 뽑아낼 수 있도록 변환
            // AuthorTag 엔티티에서 태그명만 뽑아서 Set<String>으로 변환
            dto.setTags(author.getTags().stream()
                    .map(authorTag -> authorTag.getTag().getName())
                    .collect(Collectors.toSet())
            );
        }

        // 200 OK + DTO 반환
        return ResponseEntity.ok(dto);
    }


}
