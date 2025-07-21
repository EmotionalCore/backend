package com.example.project.emotionCore.domain;


import com.example.project.emotionCore.dto.MyPageUpdateDTO;
import jakarta.persistence.*;
import lombok.*;
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
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(nullable = false, unique = true)
    private String username;               // nick

    @Column(nullable = false, unique = true)
    private String email;            //

    @Column(nullable = false)
    private String password;           // pw

    private String profileImageUrl;
    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateProfile(MyPageUpdateDTO dto) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
    }
}
