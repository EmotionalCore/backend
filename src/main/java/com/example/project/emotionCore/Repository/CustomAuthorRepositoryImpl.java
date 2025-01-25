package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.project.emotionCore.domain.QAuthor.author;
import static com.example.project.emotionCore.domain.QSeries.series;

@Repository
public class CustomAuthorRepositoryImpl implements CustomAuthorRepository {
    private final JPAQueryFactory queryFactory;
    @Autowired
    public CustomAuthorRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }



    @Override
    public List<Author> findByKeywords(List<String> keywords) {
        return List.of();
    }

    private BooleanExpression containsKeywordInDescription(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return author.description.contains(keyword);
    }

    private BooleanExpression containsKeywordInTags(String keyword){
        if(keyword == null || keyword.isEmpty()) return null;
        return author.tags.contains(keyword);
    }
}
