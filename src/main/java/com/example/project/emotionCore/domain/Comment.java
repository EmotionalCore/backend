package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="comment")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(CommentId.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Id
    @Column(name = "series_id", nullable = false)
    private Long seriesId;  // ✅ 시리즈 ID

    @Id
    @Column(name = "number", nullable = false)
    private Long number;  // ✅ 에피소드 번호

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "series_id", referencedColumnName = "series_id", insertable = false, updatable = false),
            @JoinColumn(name = "number", referencedColumnName = "number", insertable = false, updatable = false)
    })
    private Episode episode;  // ✅ `Episode`와 다대일 관계 설정

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

    public void updateContent(String newContent){
        this.commentContents=newContent;
    }


}

/*
        1시리즈 1에피소드 1번댓글
        1시리즈 2에피소드 1번댓글

 */