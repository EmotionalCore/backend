package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.SearchWorkRepository;
import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.domain.SearchWork;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.NovelAndPoemPreviewDTO;
import com.example.project.emotionCore.dto.SearchWorkDTO;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.enums.WorkType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkService {
    SearchWorkRepository searchWorkRepository;
    SeriesRepository seriesRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public WorkService(SeriesRepository seriesRepository, SearchWorkRepository searchWorkRepository) {
        this.seriesRepository = seriesRepository;
        this.searchWorkRepository = searchWorkRepository;
    }

    public List<SeriesPreviewDTO> getTodayBestSeries(int limit) {
        List<Series> seriesData = seriesRepository.findTodayBestSeries(1, limit);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<NovelAndPoemPreviewDTO> getBestLikeNovelOrPoemSeries(WorkType workType) {
        List<Series> entity = seriesRepository.findTop3ByTypeOrderByLikeCount(workType.name());
        List<NovelAndPoemPreviewDTO> data = new ArrayList<>();
        for (Series series : entity) {
            data.add(modelMapper.map(series, NovelAndPoemPreviewDTO.class));
        }
        return data;
    }

    public List<SeriesPreviewDTO> getBestLikeWebtoonSeries() {
        List<Series> entity = seriesRepository.findTop3ByTypeOrderByLikeCount(WorkType.WEBTOON.name());
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : entity) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByTag(List<String> tags) {
        List<Series> entity = seriesRepository.findAllByTagsContaining(tags);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for(Series series : entity){
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }


    public List<SearchWorkDTO> getBestSearchWork() {
        // 인기 검색어를 가져옴
        List<SearchWork> entities = searchWorkRepository.findTop10ByOrderBySearchCountDesc();
        List<SearchWorkDTO> data = new ArrayList<>();

        // 엔티티를 DTO로 변환
        for (SearchWork searchWork : entities) {
            data.add(modelMapper.map(searchWork, SearchWorkDTO.class));
        }
        return data;
    }

}
