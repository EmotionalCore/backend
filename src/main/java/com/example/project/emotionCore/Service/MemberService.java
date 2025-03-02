package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import com.example.project.emotionCore.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Destination;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final NaverService naverService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final SeriesRepository seriesRepository;


    //3
    public JwtTokenDTO singIn(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authentication);
    }

    public String singUp(SignUpDTO signUpDTO) {
        signUpDTO.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        if (memberRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new CustomBadRequestException(400, "Email already exists");
        }
        if (memberRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new CustomBadRequestException(400, "Username already exists");
        }
        try{
            memberRepository.save(MemberMapper.toEntity(signUpDTO));
        }
        catch (Exception e){
            throw new CustomBadRequestException(400, "Error code D92130. 관리자에게 연락해주세요."); //에러코드 암거나씀
        }

        return "";
    }

    public JwtTokenDTO getNewToken(String refreshToken){
        return jwtTokenProvider.refreshToken(refreshToken);
    }

    public boolean isAlreadyUsedName(String username){
        return memberRepository.existsByUsername(username);
    }


    //2
    public String signUpWithSocial(String email, String username) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setUsername(username);
        memberDTO.setPassword("");  // 소셜 로그인이라 비밀번호는 필요 없음
        memberRepository.save(MemberMapper.toEntity(memberDTO));
        return "소셜 회원가입 완료";
    }

    //11
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


        ///////////////////////////////////////////////
        // JWT Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        return jwtTokenProvider.generateToken(authentication);
    }

    // 이메일로 사용자 존재 여부 확인
    public boolean isEmailRegistered(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 소셜 로그인 시 기존 사용자 처리
    public JwtTokenDTO signInWithSocial(String email) {
        // 소셜 로그인은 비밀번호 없이 이메일만으로 로그인
        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(email, ""));
    }


    public List<SeriesViewedPreviewDTO> getViewedEpisode(Authentication authentication){
        CustomMemberDetail memberDetail = (CustomMemberDetail) authentication.getPrincipal();
        return seriesRepository.findViewListByMemberId(memberDetail.getId());
    }



    public String findPassword(MemberDTO memberDTO) {
        return "비밀번호 찾기 기능은 아직 구현되지 않았습니다.";
    }

    @Transactional
    public void updateProfileImage(Long MemberId, String profileImageUrl){
        Member member = memberRepository.findById(MemberId).orElseThrow(()->new RuntimeException("멤버 검색 불가"));

        member.updateProfileImageUrl(profileImageUrl);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long MemberId){
        if(!memberRepository.existsById(MemberId)){
            throw new IllegalArgumentException("해당 회원은 존재하지 않는 회원입니다.");
        }
        memberRepository.deleteById(MemberId);
    }

    @Transactional
    public void updateMember(Member member, String profileImageURL, String username, String email) {
        // 필요한 필드만 업데이트
        member.updateProfile(profileImageURL, username, email);
        memberRepository.save(member);  // JPA의 save 메서드는 업데이트도 처리합니다.
    }

    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
