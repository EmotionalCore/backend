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
