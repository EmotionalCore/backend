package com.example.project.emotionCore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchWorkDTO {
    private int searchId;
    private String searchWord;
    private int searchCount;
}


