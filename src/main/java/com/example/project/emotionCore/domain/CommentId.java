package com.example.project.emotionCore.domain;


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

    private Long seriesId;
    private Long number;
    private Long commentId;
}
