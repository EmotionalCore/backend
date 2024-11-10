package com.example.project.emotionCore.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(nullable = false, unique = true)
    private String email;               // 아이디로 쓸 예정 ( 로그인 시 아이디 )

    @Column(nullable = false)
    private String password;            // ㅇㅇ

    @Column(nullable = false, unique = true)
    private String userName;           // 닉네임
}