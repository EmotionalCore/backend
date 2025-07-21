package com.example.project.emotionCore.service;

import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.domain.SeriesView;
import com.example.project.emotionCore.domain.WorkViewLog;
import com.example.project.emotionCore.dto.EpisodeModifyDTO;
import com.example.project.emotionCore.dto.EpisodePreviewDTO;
import com.example.project.emotionCore.dto.EpisodeRequestDTO;
import com.example.project.emotionCore.dto.EpisodeResponseDTO;
import com.example.project.emotionCore.exception.CustomBadRequestException;
import com.example.project.emotionCore.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final AzureBlobService azureBlobService;
    private final ImageValidatorService imageValidatorService;
    private final TagService tagService;
    private final SeriesViewRepository seriesViewRepository;
    private final WorkViewLogRepository workViewLogRepository;
    private final ImageUploadService imageUploadService;
    ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void saveNewEpisode(EpisodeRequestDTO dto) { //코드 개떡
        //? toEntity, ofEntity 는 대체 어떻게 해야 깔끔 할까?
        Episode episode = Episode.builder().seriesId(dto.getSeriesId()).build();
        episodeRepository.save(episode);
        Episode savedEpisode = episodeRepository.findTopBySeriesIdOrderByCreatedAtDesc(episode.getSeriesId());

        /*
        Set<EpisodeTag> episodeTags = dto.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그: " + tagName)))
                .map(tag -> EpisodeTag.builder().tag(tag).episode(episode).build())
                .collect(Collectors.toSet());

        savedEpisode.setTags(episodeTags);
        */
        savedEpisode.update(dto, tagService);
        Episode.EpisodeKey episodeKey = Episode.EpisodeKey.builder()
                .seriesId(savedEpisode.getSeriesId())
                .number(savedEpisode.getNumber())
                .build();
        if(dto.getCoverImage() != null){
            imageUploadService.uploadImageToCloud(savedEpisode.getCoverImageUrl(), dto.getCoverImage());
        }
        if(dto.getImages().get(0) != null){
            imageUploadService.uploadImagesToCloud(episodeKey, dto.getImages());
        }


    }

    public EpisodeResponseDTO getEpisode(long seriesId, long episodeNumber, Authentication authentication){
        Episode episode = episodeRepository.findBySeriesIdAndNumber(seriesId, episodeNumber);

        //코드 개떡같음 나중에 수정하기
        increaseViewCount(seriesId, episodeNumber, authentication);
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
        episode.update(dto, tagService);

    /*
        episode.getTags().clear();

        Set<EpisodeTag> Tags = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .map(tag -> EpisodeTag.builder()
                        .episode(episode)
                        .tag(tag)
                        .build())
                .collect(Collectors.toSet());

        episode.getTags().addAll(Tags);
    */


        if(dto.getImages().get(0) != null){
            Episode.EpisodeKey episodeKey = Episode.EpisodeKey.builder()
                    .seriesId(episode.getSeriesId())
                    .number(episode.getNumber())
                    .build();
            imageUploadService.uploadImagesToCloud(episodeKey, dto.getImages());
        }
        episodeRepository.save(episode);
    }

    public List<EpisodePreviewDTO> getEpisodeList(int index, int size, long seriesId){
        Pageable pageable = PageRequest.of(index,size);

        List<EpisodePreviewDTO> dtos = new ArrayList<>();
        List<Episode> episodes = episodeRepository.findBySeriesId(pageable,seriesId);
        for(Episode episode : episodes){
            EpisodePreviewDTO dto = modelMapper.map(episode, EpisodePreviewDTO.class);
            dtos.add(dto);
        }
        return dtos;
    }

    public int getEpisodeCount(long seriesId) {
        return episodeRepository.countBySeriesId(seriesId);
    }
}
