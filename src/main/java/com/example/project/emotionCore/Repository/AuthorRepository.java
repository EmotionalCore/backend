package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>, CustomAuthorRepository {
    Author findById(long id);
}
