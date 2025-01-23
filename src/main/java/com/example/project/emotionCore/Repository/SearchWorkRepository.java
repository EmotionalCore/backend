package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.SearchWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchWorkRepository extends JpaRepository<SearchWork, Integer> {
    Optional<SearchWork> findBySearchWord(String searchWord);
    List<SearchWork> findTop10ByOrderBySearchCountDesc();
}
