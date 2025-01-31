package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;

import java.util.List;

public interface CustomAuthorRepository {
    List<Author> findMonthlyBestAuthor(int limit);
}
