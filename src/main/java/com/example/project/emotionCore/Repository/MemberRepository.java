package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> { //DB와 연결하는 역할
    public Member findById(long id);
    //public boolean existsByMemberIdAndPassword(String memberId, String password);
    public Member findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
}
