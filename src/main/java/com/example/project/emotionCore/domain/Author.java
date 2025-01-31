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
public class Author{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(name = "description")
    private String description;

    @Column(name = "links")
    private String links;

    @Column(name = "tags")
    private String tags;
    //나중에 더 추가
}
