package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.QSeries;
import com.example.project.emotionCore.domain.QSeriesView;
import com.example.project.emotionCore.domain.Series;
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
}
