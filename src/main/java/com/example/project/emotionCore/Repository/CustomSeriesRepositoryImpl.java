package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.QSeries;
import com.example.project.emotionCore.domain.QSeriesView;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.SeriesViewedPreviewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.project.emotionCore.domain.QMember.member;
import static com.example.project.emotionCore.domain.QSeries.series;
import static com.example.project.emotionCore.domain.QSeriesView.seriesView;
import static com.example.project.emotionCore.domain.QWorkViewLog.workViewLog;

@Repository
public class CustomSeriesRepositoryImpl implements CustomSeriesRepository {

    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomSeriesRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public List<Series> findNDaysTopViewSeries(LocalDate startDate, LocalDate endDate, int limit) {
        return queryFactory
                .selectFrom(series)
                .leftJoin(seriesView).on(
                        series.id.eq(seriesView.seriesId.intValue())
                                .and(seriesView.viewDate.between(startDate, endDate))
                )
                .groupBy(series.id)
                .orderBy(seriesView.count.sum().coalesce(0).desc()) // null 값을 0으로 처리
                .limit(limit)
                .fetch();
    }

    public List<Series> findByKeywords(List<String> keywords) {
        BooleanBuilder condition = new BooleanBuilder();
        for (String keyword : keywords) {
            condition.and(containsKeyword(keyword));
        }

        return queryFactory
                .selectFrom(series)
                .where(condition)
                .fetch();
    }

    @Override
    public List<Series> findTodayBestSeries(int days, int limit) { //최적화 필요할듯
        LocalDate today = LocalDate.now();
        LocalDate targetDay = today.minusDays(days);
        QSeriesView sv1 = new QSeriesView("sv1");
        QSeriesView sv2 = new QSeriesView("sv2");

        NumberExpression<Integer> countDiff =
                sv1.count.coalesce(0).subtract(sv2.count.coalesce(0));
        return queryFactory
                .selectFrom(series)
                .join(sv1).on(series.id.eq(sv1.seriesId.intValue()).and(sv1.viewDate.eq(today)))
                .leftJoin(sv2).on(series.id.eq(sv2.seriesId.intValue()).and(sv2.viewDate.eq(targetDay)))
                .where(countDiff.isNotNull())  // 차이가 있는 경우만 필터링
                .orderBy(countDiff.desc())  // 차이 기준 내림차순 정렬
                .limit(4)  // 상위 4개만
                .fetch();
    }

    @Override
    public List<Series> findAllByTagsContaining(List<String> tags) {
        QSeries series = QSeries.series;

        // 모든 태그를 포함하는 조건 생성
        BooleanExpression condition = null;
        for (String tag : tags) {
            BooleanExpression containsTag = series.tags.like("%" + tag + "%");
            condition = (condition == null) ? containsTag : condition.and(containsTag);
        }

        // Query 실행
        return queryFactory
                .selectFrom(series)
                .where(condition)
                .fetch();
    }



    private BooleanExpression containsKeyword(String keyword){
        return containsKeywordInDescription(keyword)
                .or(containsKeywordInTags(keyword))
                .or(containsKeywordInTitle(keyword))
                .or(containsKeywordInType(keyword));
    }

    private BooleanExpression containsKeywordInTitle(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return series.title.contains(keyword);
    }

    private BooleanExpression containsKeywordInDescription(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return series.description.contains(keyword);
    }

    private BooleanExpression containsKeywordInType(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return series.type.contains(keyword);
    }

    private BooleanExpression containsKeywordInTags(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return series.tags.contains(keyword);
    }

    @Override
    public List<Series> findMonthlyBestSeries(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 이번 달의 첫 날
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // 이번 달의 마지막 날

        QSeries series = QSeries.series; // Series 엔티티
        QSeriesView sv = seriesView; // SeriesView 엔티티

        return queryFactory
                .select(series) // Series 엔티티를 기준으로 선택
                .from(sv) // SeriesView 엔티티를 기준으로 시작
                .join(series).on(series.id.eq(sv.seriesId.intValue())) // Series와 SeriesView를 조인
                .where(sv.viewDate.between(firstDayOfMonth, lastDayOfMonth)) // 이번 달 범위 필터
                .groupBy(series.id) // 시리즈별로 그룹화
                .orderBy(sv.count.sum().desc()) // count 합계 기준 내림차순 정렬
                .limit(limit) // 상위 limit개의 결과만 반환
                .fetch(); // 결과 fetch
    }

    @Override
    public List<SeriesViewedPreviewDTO> findViewListByMemberId(long memberId) {
        return queryFactory
                .select(Projections.constructor(
                        SeriesViewedPreviewDTO.class,
                        series.id,
                        member.id,
                        member.username,
                        series.title,
                        series.coverImageUrl,
                        workViewLog.episodeNumber
                ))
                .from(series)
                .join(workViewLog).on(workViewLog.memberId.eq(memberId)
                        .and(series.id.eq(workViewLog.seriesId.intValue())))
                .join(member).on(series.authorInfos.id.eq(member.id))
                .fetch();
    }
}
