package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.dto.MemberDetailDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.example.project.emotionCore.domain.QAuthor.author;
import static com.example.project.emotionCore.domain.QMember.member;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public MemberDetailDTO findMemberDetailById(Long id) {
        return queryFactory
                .select(Projections.constructor(MemberDetailDTO.class,
                        member.id,
                        member.username,
                        member.email,
                        member.profileImageUrl,
                        author.description,
                        author.links,
                        author.tags
                        ))
                .from(member)
                .leftJoin(author).on(author.id.eq(member.id))
                .where(member.id.eq(id))
                .fetchOne();
    }
}
