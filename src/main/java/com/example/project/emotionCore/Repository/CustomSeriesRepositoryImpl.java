package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.QSeries;
import com.example.project.emotionCore.domain.QSeriesView;
import com.example.project.emotionCore.domain.Series;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    public List<Series> findTodayBestSeries(LocalDate today, int limit) {
        LocalDate target = today.minusDays(1);
        return queryFactory
                .selectFrom(series)
                .orderBy()
                .limit(limit)
                .fetch();
    }
}
