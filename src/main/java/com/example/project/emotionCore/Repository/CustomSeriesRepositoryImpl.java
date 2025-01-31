package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.QSeries;
import com.example.project.emotionCore.domain.QSeriesView;
import com.example.project.emotionCore.domain.Series;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.example.project.emotionCore.domain.QSeries.series;
import static com.example.project.emotionCore.domain.QSeriesView.seriesView;

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
                        series.id.eq(seriesView.series.id)
                                .and(seriesView.viewDate.between(startDate, endDate))
                )
                .groupBy(series.id)
                .orderBy(seriesView.count.sum().coalesce(0).desc()) // null 값을 0으로 처리
                .limit(limit)
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
                .join(sv1).on(series.id.eq(sv1.series.id).and(sv1.viewDate.eq(today)))
                .leftJoin(sv2).on(series.id.eq(sv2.series.id).and(sv2.viewDate.eq(targetDay)))
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
}
