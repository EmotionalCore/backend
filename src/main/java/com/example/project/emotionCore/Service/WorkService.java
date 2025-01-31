package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.AuthorRepository;
import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.Repository.SeriesRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.domain.SeriesView;
import com.example.project.emotionCore.dto.AuthorDTO;
import com.example.project.emotionCore.dto.NovelAndPoemPreviewDTO;
import com.example.project.emotionCore.dto.SeriesDetailDTO;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.module.mapper.MemberMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class WorkService {
    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    SeriesRepository seriesRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public WorkService(SeriesRepository seriesRepository, AuthorRepository authorRepository, MemberRepository memberRepository) {
        this.seriesRepository = seriesRepository;
        this.authorRepository = authorRepository;
        this.memberRepository = memberRepository;
    }

    public List<SeriesPreviewDTO> getTodayBestSeries(int limit) {
        LocalDate today = LocalDate.now();

        List<Series> seriesData = seriesRepository.findNDaysTopViewSeries(today, today, limit);
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
            NovelAndPoemPreviewDTO dto = modelMapper.map(series, NovelAndPoemPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getBestLikeWebtoonSeries() {
        List<Series> entity = seriesRepository.findTop3ByTypeOrderByLikeCount(WorkType.WEBTOON.name());
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : entity) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesDetailDTO> getSeriesByKeywords(List<String> keywords) {
        List<Series> entity = seriesRepository.findByKeywords(keywords);
        List<SeriesDetailDTO> data = new ArrayList<>();
        for (Series series : entity) {
            data.add(modelMapper.map(series, SeriesDetailDTO.class));
        }
        return data;
    }
}
