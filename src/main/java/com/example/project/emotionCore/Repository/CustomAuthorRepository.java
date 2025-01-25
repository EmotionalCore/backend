package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomAuthorRepository {
    List<Author> findByKeywords(List<String> keywords);
}
