package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.dto.AuthorDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomAuthorRepository {
    List<Author> findByKeywords(List<String> keywords);
    Author findById(long id);
    AuthorDTO findByAuthorId(long id);
    List<Author> findMonthlyBestAuthor(LocalDate startDate, LocalDate endDate, int limit);
}
