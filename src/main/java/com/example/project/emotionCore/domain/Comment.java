package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(CommentId.class)
public class Comment {

    @Id // 댓글 ID (PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Id // 시리즈 ID (단순 Long 타입으로 저장)
    @JoinColumn(name = "series_id", nullable = false)
    private Long seriesId;  // ✅ 시리즈 ID는 Long으로 저장

    @Id // 에피소드 번호
    @JoinColumn(name = "number", nullable = false)
    private Long number;

    @Column(name="comment_contents",nullable=false)
    private String commentContents; //댓글내용

    @Column(name="comment_date")
    private Date commentDate;//댓글 남긴 날짜

    @Column(name="comment_like")
    private int commentLike; // 댓글 좋아요수

    @OneToOne
    @JoinColumn(name = "member_id",nullable=false)
    private Member member; // 남긴사람

    public void setMember(Member member) {
        this.member = member;
    }

    public void increaseLike(){
        this.commentLike++;
    }

    public void decreaseLike(){
        this.commentLike--;
    }

    public void updateContent(String newContent){
        this.commentContents=newContent;
    }


}

/*
        1시리즈 1에피소드 1번댓글
        1시리즈 2에피소드 1번댓글

 */