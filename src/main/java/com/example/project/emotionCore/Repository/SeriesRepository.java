package com.example.project.emotionCore.Repository;

import com.example.project.emotionCore.domain.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer>, CustomSeriesRepository {
    public List<Series> findBy();
}
