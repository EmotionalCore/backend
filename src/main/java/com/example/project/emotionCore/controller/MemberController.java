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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

import java.util.List;

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
    public ResponseEntity<SuccessResponse<String>> signUp(@Valid @RequestBody SignUpDTO signUpDTO, BindingResult result){
        if (result.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : result.getFieldErrors()) {
                errorMessage.append(fieldError.getField())
                        .append(" 이(가) ").append(fieldError.getDefaultMessage())
                        .append("\n");
            }
            throw new CustomBadRequestException(400, errorMessage.toString());
        }
        try{
            memberService.singUp(signUpDTO);
        }
        catch (CustomBadRequestException e){
            throw new CustomBadRequestException(400, e.getMessage());
        }
        return ResponseEntity.ok(new SuccessResponse<>("회원가입에 성공했습니다."));
    }

    @Operation(
            summary = "이미 사용된 username 인지 확인"
    )

    @GetMapping("/check/username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean exists = memberService.isAlreadyUsedName(username);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공적으로 로그인"),
            @ApiResponse(responseCode = "401", description = "로그인에 실패하였음(Invalid Email or Password)", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/signin")  // 로그인
    public ResponseEntity<JwtTokenDTO> signIn(@RequestBody SigninRequestDTO signinRequestDTO) {
        try{
            JwtTokenDTO tokenDTO = memberService.singIn(signinRequestDTO.getEmail(), signinRequestDTO.getPassword());
            return ResponseEntity.ok(tokenDTO);
        }
        catch(AuthenticationException e){
            throw new CustomBadRequestException(401, e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 토큰 발급"),
            @ApiResponse(responseCode = "401", description = "잘못된 토큰일경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/token/refresh")
    public ResponseEntity<JwtTokenDTO> refreshToken(@RequestParam String refreshToken){
        JwtTokenDTO tokenDTO = memberService.getNewToken(refreshToken);
        if(tokenDTO == null){
            throw new CustomBadRequestException(401, "Invalid Refresh Token");
        }
        return ResponseEntity.ok(tokenDTO);
    }

    @Operation(description = "매번 로그인하는거 귀찮아서 만듦")
    @GetMapping("/W0W_Y0U_AR3_ADM1N")
    public ResponseEntity<JwtTokenDTO> W0W_Y0U_AR3_ADM1N(){
        JwtTokenDTO tokenDTO = memberService.singIn("test@localhost.com", "test123!");
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/findpassword")   // 비밀번호 찾기
    public String findPassword() {
        return "findpassword";
    }

    @GetMapping("/check/email")
    public ResponseEntity<Map<String,Boolean>> checkEmail(@RequestParam String email){
        boolean exists= memberService.isEmailRegistered(email);
        return ResponseEntity.ok(Collections.singletonMap("exists",exists));
    }

    @PutMapping("/{memberId}/profile-image")
    public ResponseEntity<String> updateProfileImage(
            @PathVariable Long memberId,
            @RequestBody Map<String, String> request) {

        String newProfileImageUrl = request.get("profileImageUrl");
        memberService.updateProfileImage(memberId, newProfileImageUrl);
        return ResponseEntity.ok("프로필 이미지가 업데이트되었습니다.");
    }






    @Operation(summary = "특정 유저가 최근에 본 작품 기록 반환")
    @GetMapping("/list/view")
    public ResponseEntity<List<SeriesViewedPreviewDTO>> getViewedEpisode(Authentication authentication){
        List<SeriesViewedPreviewDTO> data = memberService.getViewedEpisode(authentication);
        return ResponseEntity.ok(data);
    }





}
