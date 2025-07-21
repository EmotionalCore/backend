package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.*;
import com.example.project.emotionCore.dto.SeriesViewedPreviewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
                        series.id.eq(seriesView.seriesId)
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
                .join(sv1).on(series.id.eq(sv1.seriesId).and(sv1.viewDate.eq(today)))
                .leftJoin(sv2).on(series.id.eq(sv2.seriesId).and(sv2.viewDate.eq(targetDay)))
                .where(countDiff.isNotNull())  // 차이가 있는 경우만 필터링
                .orderBy(countDiff.desc())  // 차이 기준 내림차순 정렬
                .limit(4)  // 상위 4개만
                .fetch();
    }
    @Override
    public List<Series> findAllByTagsContaining(List<String> tags) {
        QSeries series = QSeries.series;
        QSeriesTag seriesTag = QSeriesTag.seriesTag;
        QTag tag = QTag.tag;

        return queryFactory
                .select(series)
                .from(series)
                .join(series.tags, seriesTag)
                .join(seriesTag.tag, tag)
                .where(tag.name.in(tags))
                .groupBy(series.id)
                .having(tag.name.countDistinct().eq((long) tags.size()))
                .fetch();
    }

    @Override
    public int countByTags(List<String> tags) {
        QSeries series = QSeries.series;
        QSeriesTag seriesTag = QSeriesTag.seriesTag;
        QTag tag = QTag.tag;

        List<Long> result = queryFactory
                .select(series.id)
                .from(series)
                .join(series.tags, seriesTag)
                .join(seriesTag.tag, tag)
                .where(tag.name.in(tags))
                .groupBy(series.id)
                .having(tag.name.countDistinct().eq((long) tags.size()))
                .fetch();

        return result.size();
    }

    public List<Series> findAllByTypeAndTags(Pageable pageable, String type, List<String> tags) {
        QSeries series = QSeries.series;
        QSeriesTag seriesTag = QSeriesTag.seriesTag;
        QTag tag = QTag.tag;

        BooleanBuilder builder = new BooleanBuilder();

        if(!type.equals("전체")){
            builder.and(series.type.eq(type));
        }

        builder.and(tag.name.in(tags));

        return queryFactory
                .select(series)
                .from(series)
                .join(series.tags, seriesTag)
                .join(seriesTag.tag, tag)
                .where(builder)
                .groupBy(series.id)
                .having(tag.name.countDistinct().eq((long) tags.size()))
                .orderBy(series.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public int countByTypeAndTags(String type, List<String> tags) {
        QSeries series = QSeries.series;
        QSeriesTag seriesTag = QSeriesTag.seriesTag;
        QTag tag = QTag.tag;

        BooleanBuilder builder = new BooleanBuilder();

        if (!type.equals("전체")) {
            builder.and(series.type.eq(type));
        }

        if (tags != null && !tags.isEmpty()) {
            builder.and(tag.name.in(tags));
        }

        return queryFactory
                .select(series.id)
                .from(series)
                .join(series.tags, seriesTag)
                .join(seriesTag.tag, tag)
                .where(builder)
                .groupBy(series.id)
                .having(tag.name.countDistinct().eq((long) tags.size()))
                .fetch()
                .size();
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
        return series.tags.any().tag.name.containsIgnoreCase(keyword);
    }

    @Override
    public List<Series> findMonthlyBestSeries(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDate lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        QSeries series = QSeries.series;
        QSeriesView sv = seriesView;

        return queryFactory
                .select(series)
                .from(sv)
                .join(series).on(series.id.eq(sv.seriesId))
                .where(sv.viewDate.between(firstDayOfMonth, lastDayOfMonth))
                .groupBy(series.id)
                .orderBy(sv.count.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public void addLikeCount(Series selectedSeries) {
        queryFactory.update(series)
                .set(series.likeCount, series.likeCount.add(1))
                .where(series.eq(selectedSeries))
                .execute();
    }

    @Override
    public void subLikeCount(Series selectedSeries) {
        queryFactory.update(series)
                .set(series.likeCount, series.likeCount.subtract(1))
                .where(series.eq(selectedSeries))
                .execute();
    }

    @Override
    public void addBookMarkCount(Series selectedSeries) {
        queryFactory.update(series)
                .set(series.bookmarkCount, series.bookmarkCount.add(1))
                .where(series.eq(selectedSeries))
                .execute();
    }

    @Override
    public void subBookMarkCount(Series selectedSeries) {
        queryFactory.update(series)
                .set(series.bookmarkCount, series.bookmarkCount.subtract(1))
                .where(series.eq(selectedSeries))
                .execute();
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
                        .and(series.id.eq(workViewLog.seriesId)))
                .join(member).on(series.authorId.eq(member.id))
                .fetch();
    }
}