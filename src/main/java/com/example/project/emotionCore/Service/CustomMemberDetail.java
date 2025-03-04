package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomMemberDetail implements UserDetails {
    private Long id; // 사용자 고유 ID
    private String username;
    private String password;
    private String email; // 추가 필드

    private Collection<? extends GrantedAuthority> authorities;

    // 생성자
    public CustomMemberDetail(Long id, String password, String email, String username,
                              Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.email = email;
        this.authorities = authorities;
    }

    // 추가 메서드
    public Long getId() {return id;}

    public String getEmail() {
        return email;
    }

    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정이 만료되지 않았는지 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않았는지 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명이 만료되지 않았는지 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

}


/*
Member -> userDetails
CustomMemberDetail -> userDetails
JWTProvider- > CustomMemberDetail -> userDetails
-> member? 어디서 얻어옴 ? 우리는 JWT에서 받아와야함.
member에 저장된 db속에서 구해올 아이디가 필요한대.
그 db를 들여다보는 의존관계가 단 1개도 없음.
 */
