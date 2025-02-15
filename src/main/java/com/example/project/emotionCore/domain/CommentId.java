package com.example.project.emotionCore.domain;


import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class CommentId implements Serializable {
    private Long seriesId;  // ✅ `Episode`와 연결된 필드
    private Long number;    // ✅ `Episode`와 연결된 필드
    private Long commentId; // ✅ 고유한 댓글 ID
}
