package com.example.project.emotionCore.service;

import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new CustomMemberDetail(
                member.getId(),
                member.getPassword(),
                member.getEmail(),
                member.getUsername(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 설정
        );
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new CustomMemberDetail(
                member.getId(),
                member.getPassword(),
                member.getEmail(),
                member.getUsername(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 설정
        );
    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return new CustomMemberDetail(
                member.getId(),
                member.getPassword(),
                member.getEmail(),
                member.getUsername(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // 권한 설정
        );
    }
}
