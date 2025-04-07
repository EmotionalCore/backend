package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.*;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.dto.AuthorDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.project.emotionCore.domain.QAuthor.author;
import static com.example.project.emotionCore.domain.QMember.member;
import static com.example.project.emotionCore.domain.QSeries.series;
import static com.example.project.emotionCore.domain.QSeriesView.seriesView;

@Repository
public class CustomAuthorRepositoryImpl implements CustomAuthorRepository{

    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomAuthorRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public AuthorDTO findByAuthorId(long id) {
            return queryFactory.select(Projections.constructor(AuthorDTO.class,
                author.id, member.username, author.description, author.links, author.tags))
                .from(author)
                .where(author.id.eq(id))
                .join(member).on(author.id.eq(member.id))
                .fetchFirst();
    }

    @Override
    public List<Author> findByMonthlyBestAuthor(LocalDate startDate, LocalDate endDate, int limit) {
        return queryFactory
                .selectFrom(author)
                .join(author.seriesList, series)
                .leftJoin(seriesView).on(
                        series.id.eq(seriesView.seriesId.intValue())
                                .and(seriesView.viewDate.between(startDate, endDate)))
                .groupBy(author.id) // 작가별 그룹화
                .orderBy(series.likeCount.sum().coalesce(0).desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<Author> findByKeywords(List<String> keywords) {
        BooleanBuilder condition = new BooleanBuilder();
        for (String keyword : keywords) {
            condition.and(containsKeyword(keyword));
        }
        QAuthor author = QAuthor.author; // Author 엔티티
        return queryFactory
                .selectFrom(author)
                .join(member).on(member.id.eq(author.id))
                .where(condition)
                .fetch();
    }

    private BooleanExpression containsKeyword(String keyword) {
        return containsKeywordInDescription(keyword)
                .or(containsKeywordInTags(keyword))
                .or(containsKeywordInUsername(keyword));
    }

    private BooleanExpression containsKeywordInDescription(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return author.description.contains(keyword);
    }

    private BooleanExpression containsKeywordInTags(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return author.tags.any().tag.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression containsKeywordInUsername(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return member.username.contains(keyword);
    }
}
