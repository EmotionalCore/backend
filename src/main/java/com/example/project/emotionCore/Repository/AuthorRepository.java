package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAllByOrderByIdDesc(Pageable pageable);
    Author findById(long id);
}
