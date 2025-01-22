package com.example.project.emotionCore.controller;

import com.example.project.emotionCore.Service.MemberService;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(
            summary = "회원가입."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 가입함"),
            @ApiResponse(responseCode = "400", description = "회원가입에 실패하였음(message에 원인 표시)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signup")    // 등록
    public ResponseEntity<SuccessResponse<String>> signUp(@Valid @RequestBody MemberDTO memberDTO, BindingResult result){
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : result.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(" 이(가) ").append(fieldError.getDefaultMessage())
                        .append("\n");
            }
            throw new CustomBadRequestException(400, errorMessage.toString());
        }
        memberService.singUp(memberDTO);
        SuccessResponse<String> successResponse = new SuccessResponse<>("회원가입에 성공하였습니다.");
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/signin")  // 로그인
    public ResponseEntity<?> signIn(@RequestBody SigninRequestDTO signinRequestDTO) {
        try{
            JwtTokenDTO tokenDTO = memberService.singIn(signinRequestDTO.getEmail(), signinRequestDTO.getPassword());
            return ResponseEntity.ok(tokenDTO);
        }
        catch(AuthenticationException e){
            return ResponseEntity.status(401).body(new ErrorResponse(400,"Invalid Email or Password"));
        }
    }

    @PostMapping("/findpassword")   // 비밀번호 찾기
    public String findPassword() {
        return "findpassword";
    }
}
