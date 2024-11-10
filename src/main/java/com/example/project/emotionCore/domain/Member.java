package com.example.project.emotionCore.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(nullable = false, unique = true)
    private String nickname;               // nick

    @Column(nullable = false, unique = true)
    private String email;            //

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;           // id

    @Column(nullable = false)
    private String password;           // pw
}
