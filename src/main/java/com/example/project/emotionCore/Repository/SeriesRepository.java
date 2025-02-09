package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long>, CustomSeriesRepository {
    public List<Series> findTop6ByTypeOrderByLikeCount(String type);

    public List<Series> findAllByOrderByIdDesc(Pageable pageable);

    public List<Series> findAllByTypeOrderByIdDesc(String type, Pageable pageable);
}

