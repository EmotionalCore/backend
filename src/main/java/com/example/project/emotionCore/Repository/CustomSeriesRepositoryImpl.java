package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.QSeries;
import com.example.project.emotionCore.domain.QSeriesView;
import com.example.project.emotionCore.domain.Series;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.project.emotionCore.domain.QSeries.series;

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

    @Override
    public List<Series> findMonthlyBestSeries(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1); // 이번 달의 첫 날
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // 이번 달의 마지막 날

        QSeries series = QSeries.series; // Series 엔티티
        QSeriesView sv = QSeriesView.seriesView; // SeriesView 엔티티

        return queryFactory
                .select(series) // Series 엔티티를 기준으로 선택
                .from(sv) // SeriesView 엔티티를 기준으로 시작
                .join(series).on(series.id.eq(sv.series.id)) // Series와 SeriesView를 조인
                .where(sv.viewDate.between(firstDayOfMonth, lastDayOfMonth)) // 이번 달 범위 필터
                .groupBy(series.id) // 시리즈별로 그룹화
                .orderBy(sv.count.sum().desc()) // count 합계 기준 내림차순 정렬
                .limit(limit) // 상위 limit개의 결과만 반환
                .fetch(); // 결과 fetch
    }
}
