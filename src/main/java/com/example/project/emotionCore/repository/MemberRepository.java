package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository{ //DB와 연결하는 역할

    //public boolean existsByMemberIdAndPassword(String memberId, String password);
    Member findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsername(String username);
    Optional<Member> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}