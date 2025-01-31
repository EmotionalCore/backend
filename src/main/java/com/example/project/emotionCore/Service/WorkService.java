package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.AuthorRepository;
import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.domain.SeriesView;
import com.example.project.emotionCore.dto.AuthorPreviewDTO;
import com.example.project.emotionCore.dto.NovelAndPoemPreviewDTO;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class WorkService {
    SeriesRepository seriesRepository;
    AuthorRepository authorRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public WorkService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
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

    public List<SeriesPreviewDTO> getMonthlyBestSeries(int limit) {
        List<Series> seriesData = seriesRepository.findMonthlyBestSeries(limit);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<AuthorPreviewDTO> getMonthlyBestAuthor(int limit) {
        List<Author> authorsData = authorRepository.findMonthlyBestAuthor(limit);
        List<AuthorPreviewDTO> data = new ArrayList<>();
        for (Author authors : authorsData) {
            data.add(modelMapper.map(authors, AuthorPreviewDTO.class));
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByCreatedDate(int index, int size) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = seriesRepository.findAllByOrderByIdDesc(pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByType(int index, int size, String type) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = seriesRepository.findAllByTypeOrderByIdDesc(type,pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<SeriesPreviewDTO> getNewSeries(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Series> seriesData = seriesRepository.findAllByOrderByIdDesc(pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            data.add(modelMapper.map(series, SeriesPreviewDTO.class));
        }
        return data;
    }

    public List<AuthorPreviewDTO> getNewAuthors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Author> authorData = authorRepository.findBySeriesListIsNotEmptyOrderByIdDesc(pageable);
        List<AuthorPreviewDTO> data = new ArrayList<>();

        for (Author author :authorData) {
            data.add(modelMapper.map(author, AuthorPreviewDTO.class));
        }
        return data;
    }
}
