package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.*;
import com.example.project.emotionCore.domain.*;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.dto.SearchWorkDTO;
import com.example.project.emotionCore.enums.WorkType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class WorkService {
    private final SearchWorkRepository searchWorkRepository;
    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;
    private final SeriesRepository seriesRepository;
    private final EpisodeRepository episodeRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public WorkService(SeriesRepository seriesRepository, SearchWorkRepository searchWorkRepository, AuthorRepository authorRepository, MemberRepository memberRepository, EpisodeRepository episodeRepository) {
        this.seriesRepository = seriesRepository;
        this.searchWorkRepository = searchWorkRepository;
        this.authorRepository = authorRepository;
        this.memberRepository = memberRepository;
        this.episodeRepository = episodeRepository;
    }

    public List<SeriesPreviewDTO> getTodayBestSeries(int limit) {
        LocalDate today = LocalDate.now();
        List<Series> seriesData = seriesRepository.findNDaysTopViewSeries(today, today, limit);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<NovelAndPoemPreviewDTO> getBestLikeNovelOrPoemSeries(WorkType workType) {
        List<Series> entity = seriesRepository.findTop6ByTypeOrderByLikeCount(workType.name());
        List<NovelAndPoemPreviewDTO> data = new ArrayList<>();
        for (Series series : entity) {
            NovelAndPoemPreviewDTO dto = modelMapper.map(series, NovelAndPoemPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getBestLikeWebtoonSeries() {
        List<Series> entity = seriesRepository.findTop6ByTypeOrderByLikeCount(WorkType.WEBTOON.name());
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
            SeriesDetailDTO dto = modelMapper.map(series, SeriesDetailDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByTag(List<String> tags) {
        List<Series> entity = seriesRepository.findAllByTagsContaining(tags);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : entity) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
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


    public List<SeriesPreviewDTO> getMonthlyBestSeries(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(30);
        List<Series> seriesData = seriesRepository.findNDaysTopViewSeries(today, nextMonth, limit);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByCreatedDate(int index, int size) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = seriesRepository.findAllByOrderByIdDesc(pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByType(int index, int size, String type) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = seriesRepository.findAllByTypeOrderByIdDesc(type,pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getNewSeries(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Series> seriesData = seriesRepository.findAllByOrderByIdDesc(pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<AuthorPreviewDTO> getNewAuthors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Author> authorData = authorRepository.findAllByOrderByIdDesc(pageable);
        List<AuthorPreviewDTO> data = new ArrayList<>();

        for (Author author :authorData) {
            data.add(modelMapper.map(author, AuthorPreviewDTO.class));
        }
        return data;
    }





    //Episode Start

    public void saveNewEpisode(EpisodeRequestDTO episodeRequestDTO) {
        Episode episode = new Episode();
        modelMapper.map(episodeRequestDTO, episode);
        episodeRepository.save(episode);
    }








}
