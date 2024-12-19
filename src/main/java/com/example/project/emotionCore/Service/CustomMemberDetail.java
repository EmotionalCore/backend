package com.example.project.emotionCore.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomMemberDetail implements UserDetails {
    private Long id; // 사용자 고유 ID
    private String memberId;
    private String password;
    private String email; // 추가 필드

    private Collection<? extends GrantedAuthority> authorities;

    // 생성자
    public CustomMemberDetail(Long id, String memberId, String password, String email,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    // 추가 메서드
    public Long getId() {
        return id;
    }

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
        return memberId;
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
