package com.example.project.emotionCore.service;

import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.repository.AuthorRepository;
import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.repository.SeriesRepository;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import com.example.project.emotionCore.repository.WorkViewLogRepository;
import com.example.project.emotionCore.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final CustomUserDetailService customUserDetailService;
    private final AuthorRepository authorRepository;
    private final WorkViewLogRepository workViewLogRepository;


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
            Member savedMember=memberRepository.save(MemberMapper.toEntity(signUpDTO));

            Author author = new Author(savedMember, "", "", new HashSet<>());
            authorRepository.save(author);
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
    public JwtTokenDTO signUpWithSocial(String email, String username) {
        String finalUsername = username;
        if (memberRepository.existsByUsername(finalUsername)) {
            Random random = new Random();
            do {
                int randomNumber = random.nextInt(10000); // 0~9999
                finalUsername = username + randomNumber;
            } while (memberRepository.existsByUsername(finalUsername));
        }

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(email);
        memberDTO.setUsername(finalUsername);
        memberDTO.setPassword("");  // 소셜 로그인이라 비밀번호는 필요 없음
        Member savedMember = memberRepository.save(MemberMapper.toEntity(memberDTO));
        Author author = new Author(savedMember,"", "", new HashSet<>());
        authorRepository.save(author);
        return signInWithSocial(email);
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
                    // 닉네임 중복 시 랜덤 숫자 붙이기
                    String finalUsername = nickname;
                    if (memberRepository.existsByUsername(finalUsername)) {
                        Random random = new Random();
                        do {
                            int randomNumber = random.nextInt(10000); // 0~9999
                            finalUsername = nickname + randomNumber;
                        } while (memberRepository.existsByUsername(finalUsername));
                    }

                    // Member 저장
                    Member newMember = Member.builder()
                            .username(finalUsername)
                            .email(email)
                            .password("NaverLogin") // 비밀번호는 네이버 로그인의 경우 사용하지 않음
                            .build();
                    Member savedMember = memberRepository.save(newMember);

                    // Author 저장 (MapsId 매핑)
                    Author author = new Author(savedMember, "", "", new HashSet<>());
                    authorRepository.save(author);

                    return savedMember;
                });


        ///////////////////////////////////////////////
        // JWT Token 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomMemberDetail(member), null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
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
        UserDetails userDetails = customUserDetailService.loadUserByEmail(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        return jwtTokenProvider.generateToken(authentication);
    }

    public List<SeriesViewedPreviewDTO> getViewedEpisode(int index, int size, Authentication authentication){
        Pageable pageable = PageRequest.of(index,size);
        CustomMemberDetail memberDetail = (CustomMemberDetail) authentication.getPrincipal();
        return seriesRepository.findViewListByMemberId(pageable,memberDetail.getId());
    }

    public int getViewedEpisodeCount(Authentication authentication) {
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        Long memberId = customMemberDetail.getId();
        return workViewLogRepository.countByMemberId(memberId);
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
    public void updateMember(Long memberId, MyPageUpdateDTO myPageUpdateDTO) {
        // 필요한 필드만 업데이트
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();
        member.updateProfile(myPageUpdateDTO);
        memberRepository.save(member);  // JPA의 save 메서드는 업데이트도 처리합니다.
    }

    public Optional<Member> getMemberById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public MemberDetailDTO getMemberDetail(Long memberId){
        return memberRepository.findMemberDetailById(memberId);
    }

}
