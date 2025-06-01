package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Tag;
import com.example.project.emotionCore.dto.MemberDetailDTO;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.project.emotionCore.domain.QAuthor.author;
import static com.example.project.emotionCore.domain.QAuthorTag.authorTag;
import static com.example.project.emotionCore.domain.QMember.member;

@Repository
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public MemberDetailDTO findMemberDetailById(Long id) {
        Tuple baseData = queryFactory
                .select(member.id,
                        member.username,
                        member.email,
                        member.profileImageUrl,
                        author.description,
                        author.links)
                .from(member)
                .leftJoin(author).on(author.id.eq(member.id))
                .where(member.id.eq(id))
                .fetchOne();

        Set<String> tags = new HashSet<>(queryFactory
                .select(authorTag.tag.name)
                .from(authorTag)
                .where(authorTag.author.id.eq(id))
                .fetch());

        return new MemberDetailDTO(
                baseData.get(member.id),
                baseData.get(member.username),
                baseData.get(member.email),
                baseData.get(member.profileImageUrl),
                baseData.get(author.description),
                baseData.get(author.links),
                tags
        );
    }
}
