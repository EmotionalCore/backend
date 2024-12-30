package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.controller.NaverService;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.JwtTokenDTO;
import com.example.project.emotionCore.dto.MemberDTO;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import com.example.project.emotionCore.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final NaverService naverService;
    private final AuthenticationManager authenticationManager;

    /**
     * 이메일과 비밀번호로 로그인
     *
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return JwtTokenDTO (AccessToken, RefreshToken 포함)
     */
    public JwtTokenDTO signIn(String email, String password) {
        // 사용자 인증
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * 회원가입
     *
     * @param memberDTO 회원가입 정보
     * @return 성공 메시지
     */
    public String signUp(MemberDTO memberDTO) {
        // 비밀번호 암호화 후 저장
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        memberRepository.save(MemberMapper.toEntity(memberDTO));
        return "회원가입이 완료되었습니다.";
    }

    /**
     * 네이버 로그인을 통해 사용자 인증 및 JWT 발급
     *
     * @param code  네이버에서 제공한 인증 코드
     * @param state 네이버 로그인 요청 상태값
     * @return JwtTokenDTO (AccessToken, RefreshToken 포함)
     */
    public JwtTokenDTO naverLogin(String code, String state) {
        // 네이버 API를 통해 AccessToken 가져오기
        String accessToken = naverService.getAccessToken(code, state);

        // 네이버 API를 통해 사용자 정보 가져오기
        Map<String, Object> userInfo = naverService.getUserInfo(accessToken);
        String email = userInfo.get("email").toString();
        String nickname = userInfo.get("nickname") != null ? userInfo.get("nickname").toString() : "NaverUser";

        // DB에서 사용자 확인, 없으면 신규 생성
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> {
                    Member newMember = Member.builder()
                            .username(nickname)
                            .email(email)
                            .password("NaverLogin") // 비밀번호는 네이버 로그인의 경우 사용하지 않음
                            .build();
                    return memberRepository.save(newMember);
                });

        // JWT Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        return jwtTokenProvider.generateToken(authentication);
    }

    /**
     * 비밀번호 찾기 (현재는 구현되지 않음)
     *
     * @param memberDTO 사용자 정보
     * @return 메시지
     */
    public String findPassword(MemberDTO memberDTO) {
        return "비밀번호 찾기 기능은 아직 구현되지 않았습니다.";
    }
}
