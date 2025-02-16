package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.*;
import com.example.project.emotionCore.domain.*;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.dto.SearchWorkDTO;
import com.example.project.emotionCore.enums.WorkType;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final SeriesViewRepository seriesViewRepository;
    private final WorkViewLogRepository workViewLogRepository;
    ModelMapper modelMapper = new ModelMapper();
    @Autowired
    public WorkService(SeriesRepository seriesRepository, SearchWorkRepository searchWorkRepository, AuthorRepository authorRepository, MemberRepository memberRepository, EpisodeRepository episodeRepository, SeriesViewRepository seriesViewRepository, WorkViewLogRepository workViewLogRepository) {
        this.seriesRepository = seriesRepository;
        this.searchWorkRepository = searchWorkRepository;
        this.authorRepository = authorRepository;
        this.memberRepository = memberRepository;
        this.episodeRepository = episodeRepository;
        this.seriesViewRepository = seriesViewRepository;
        this.workViewLogRepository = workViewLogRepository;
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

    public List<AuthorPreviewDTO> getMonthlyBestAuthor(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(30);
        List<Author> authorsData = authorRepository.findMonthlyBestAuthor(today,nextMonth,limit);
        List<AuthorPreviewDTO> data = new ArrayList<>();
        for (Author authors : authorsData) {
            AuthorPreviewDTO dto = modelMapper.map(authors, AuthorPreviewDTO.class);
            dto.setName(memberRepository.findById((long) dto.getId()).get().getUsername());
            dto.setCoverImageUrl(memberRepository.findById((long) dto.getId()).get().getProfileImageUrl());
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

        List<Author> authorsData = authorRepository.findBySeriesListIsNotEmptyOrderByIdDesc(pageable);
        List<AuthorPreviewDTO> data = new ArrayList<>();

        for (Author author :authorsData) {
            AuthorPreviewDTO dto = modelMapper.map(author, AuthorPreviewDTO.class);
            dto.setName(memberRepository.findById((long) dto.getId()).get().getUsername());
            dto.setCoverImageUrl(memberRepository.findById((long) dto.getId()).get().getProfileImageUrl());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesIdAndNameDTO> getMySeries(Authentication authentication){
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        List<Series> entity = seriesRepository.findAllByAuthorInfos_Id(customMemberDetail.getId());

        List<SeriesIdAndNameDTO> data = new ArrayList<>();
        for (Series series : entity) {
            SeriesIdAndNameDTO dto = modelMapper.map(series, SeriesIdAndNameDTO.class);
            data.add(dto);
        }
        return data;
    }




    //Episode Start

    public void saveNewEpisode(EpisodeRequestDTO dto) {
        //? toEntity, ofEntity 는 대체 어떻게 해야 깔끔 할까?
        Episode episode = Episode.builder()
                        .seriesId(dto.getSeriesId())
                        .title(dto.getTitle())
                        .coverImageUrl(dto.getCoverImageUrl())
                        .contents(dto.getContents())
                        .description(dto.getDescription())
                        .tags(dto.getTags())
                        .build();
        System.out.println(episode);
        //? 왜 save 전에 select 쿼리가 실행 될까?
        episodeRepository.save(episode);
    }

    public EpisodeResponseDTO getEpisode(long seriesId, long number, Authentication authentication){
        Episode episode = episodeRepository.findBySeriesIdAndNumber(seriesId, number);
        
        //코드 개떡같음 나중에 수정하기
        increaseViewCount(seriesId, number, authentication);
        episode.incrementViewCount();
        episodeRepository.save(episode);

        return modelMapper.map(episode, EpisodeResponseDTO.class);
    }

    private void increaseViewCount(long seriesId, long episodeNumber, Authentication authentication){
        SeriesView seriesView = seriesViewRepository.findBySeriesIdAndViewDate(seriesId, LocalDate.now())
                .orElseGet(() -> { //없으면 생성해서 save
                    SeriesView sv = SeriesView.builder()
                            .seriesId(seriesId)
                            .viewDate(LocalDate.now())
                            .build();
                    return seriesViewRepository.save(sv);
                });
        seriesView.incrementCount();
        seriesViewRepository.save(seriesView);

        if (authentication != null) {
            CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
            WorkViewLog log =  workViewLogRepository.findBySeriesIdAndEpisodeNumberAndMemberId(seriesId, episodeNumber, customMemberDetail.getId())
                    .orElseGet(() -> {
                        WorkViewLog newLog = WorkViewLog.builder()
                                .seriesId(seriesId)
                                .episodeNumber(episodeNumber)
                                .memberId(customMemberDetail.getId())
                                .build();
                        return workViewLogRepository.save(newLog);
                    });
            log.updateEpisodeNumber(episodeNumber);
            workViewLogRepository.save(log);
        }

    }

    @Transactional
    public void deleteEpisode(long seriesId, long number){
        episodeRepository.deleteBySeriesIdAndNumber(seriesId, number);
    }

    @Transactional
    public void updateEpisode(EpisodeModifyDTO dto){
        Episode episode = episodeRepository.findBySeriesIdAndNumber(dto.getSeriesId(), dto.getNumber());
        episode.setTitle(dto.getTitle());
        episode.setCoverImageUrl(dto.getCoverImageUrl());
        episode.setContents(dto.getContents());
        episode.setDescription(dto.getDescription());
        episode.setTags(dto.getTags());
        episodeRepository.save(episode);
    }

    public boolean isOwner(long seriesId, long memberId){
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new CustomBadRequestException(404, "Series not found"));
        return series.getAuthorInfos().getId().equals(memberId);
    }







}
