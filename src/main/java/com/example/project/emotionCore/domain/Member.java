package com.example.project.emotionCore.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="members")
public class Member implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(nullable = false, unique = true)
    private String username;               // nick

    @Column(nullable = false, unique = true)
    private String email;            //

    @Column(nullable = false)
    private String password;           // pw

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 기본 권한 ROLE_USER 반환
        return List.of(() -> "ROLE_USER");
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
        return true; // 계정이 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정이 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호가 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정이 활성화 상태임
    }
}
