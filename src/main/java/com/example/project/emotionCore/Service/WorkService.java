package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkService {
    SeriesRepository seriesRepository;

    @Autowired
    public WorkService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public List<SeriesPreviewDTO> getTodayBestSeries() {
        List<SeriesPreviewDTO> tes;
        return null;
    }
}
