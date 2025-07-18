package com.example.project.emotionCore.repository;

import com.example.project.emotionCore.domain.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer>, CustomAuthorRepository {
    Author findById(long id);
    List<Author> findBySeriesListIsNotEmptyOrderByIdDesc(Pageable pageable);
}
