package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.example.project.emotionCore.domain.QComment.comment;

@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public CustomCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public void addLikeCount(Comment selectedcomment) {
        queryFactory.update(comment)
                .set(comment.commentLike, comment.commentLike.add(1))
                .where(comment.eq(selectedcomment))
                .execute();
    }

    @Override
    public void subLikeCount(Comment selectedcomment) {
        queryFactory.update(comment)
                .set(comment.commentLike, comment.commentLike.subtract(1))
                .where(comment.eq(selectedcomment))
                .execute();
    }
}
