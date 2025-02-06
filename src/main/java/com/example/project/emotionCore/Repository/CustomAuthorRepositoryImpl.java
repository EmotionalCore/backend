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

@Repository
public class CustomAuthorRepositoryImpl implements CustomAuthorRepository{

    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomAuthorRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Author findById(long id) {
        return (Author) queryFactory
                .select(author.id, member.username, author.description, author.tags, author.links)  // member.username을 선택
                .from(author)
                .join(member).on(author.id.eq(member.id))
                .where(author.id.eq(id))
                .fetchFirst();
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
    public List<Author> findMonthlyBestAuthor(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 이번 달 첫 날
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // 이번 달 마지막 날

    @Override
    public List<Author> findByKeywords(List<String> keywords) {
        BooleanBuilder condition = new BooleanBuilder();
        for (String keyword : keywords) {
            condition.and(containsKeyword(keyword));
        }
        QAuthor author = QAuthor.author; // Author 엔티티
        QSeries series = QSeries.series; // Series 엔티티
        QSeriesView seriesView = QSeriesView.seriesView; // SeriesView 엔티티

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
                .select(author) // Author 엔티티 기준으로 선택
                .from(seriesView) // SeriesView 기준으로 시작
                .join(series).on(series.id.eq(seriesView.series.id)) // Series와 SeriesView 조인
                .join(author).on(author.id.eq(series.authorInfos.id)) // Author와 Series 조인
                .where(seriesView.viewDate.between(firstDayOfMonth, lastDayOfMonth)) // 이번 달 범위 필터
                .groupBy(author.id) // 작가별로 그룹화
                .orderBy(series.likeCount.sum().desc()) // 좋아요 수 합계 기준 내림차순 정렬
                .limit(limit) // 상위 limit명 결과만 반환
                .fetch(); // 결과 fetch
    }

    private BooleanExpression containsKeywordInTags(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return author.tags.contains(keyword);
    }

    private BooleanExpression containsKeywordInUsername(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return member.username.contains(keyword);
    }
}
