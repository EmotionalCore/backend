package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int searchId;

    @Column(nullable = false, name="search_word", unique=true)
    private String searchWord;

    @Column(nullable = false, name="search_count")
    private int searchCount=1;

    public void incrementSearchCount() {
        this.searchCount++;
    }

    public SearchWork(String searchWord) {
        this.searchWord = searchWord;
    }
}
