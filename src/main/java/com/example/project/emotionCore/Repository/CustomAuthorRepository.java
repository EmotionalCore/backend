package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;

import java.util.List;

@Repository
public interface CustomAuthorRepository {
    List<Author> findByKeywords(List<String> keywords);
    Author findById(long id);
    AuthorDTO findByAuthorId(long id);
    List<Author> findMonthlyBestAuthor(int limit);
}
