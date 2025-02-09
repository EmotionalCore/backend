package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //댓글번호 -> 나중에 3중합.

    //어떤 에피소드인지
    // 1시리즈 2에피소드 // 2시리즈 2에피소드 -> 2에피소드
    //예제는 SeriesViewId

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
}
