package com.example.project.emotionCore.service;

import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.domain.Tag;
import com.example.project.emotionCore.dto.*;
import com.example.project.emotionCore.enums.WorkType;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final MemberRepository memberRepository;
    private final SeriesRepository seriesRepository;
    private final LikeRepository likeRepository;
    private final BookMarkRepository bookMarkRepository;
    private final AzureBlobService azureBlobService;
    private final ImageValidatorService imageValidatorService;
    private final TagService tagService;
    private final ImageUploadService imageUploadService;
    ModelMapper modelMapper = new ModelMapper();

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
            Set<String> tagNames = series.getTags().stream()
                    .map(seriesTag -> seriesTag.getTag().getName())
                    .collect(Collectors.toSet());
            dto.setTags(tagNames);

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


    public List<SeriesIdAndNameDTO> getMySeries(Authentication authentication){
        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        List<Series> entity = seriesRepository.findAllByAuthorId(customMemberDetail.getId());

        List<SeriesIdAndNameDTO> data = new ArrayList<>();
        for (Series series : entity) {
            SeriesIdAndNameDTO dto = modelMapper.map(series, SeriesIdAndNameDTO.class);
            data.add(dto);
        }
        return data;
    }

    @Transactional
    public void saveNewSeries(SeriesRequestDTO dto, Authentication authentication){
        Series series = Series.builder().build();
        seriesRepository.save(series);
        series = seriesRepository.findTopByOrderByIdDesc();

        CustomMemberDetail customMemberDetail = (CustomMemberDetail) authentication.getPrincipal();
        long authorId = customMemberDetail.getId();

        Set<Tag> tagEntities = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .collect(Collectors.toSet());

        series.updateSeries(dto.getTitle(),dto.getDescription(),dto.getType(),tagEntities,dto.getImage(), authorId);
        seriesRepository.saveAndFlush(series);
        imageUploadService.uploadImageToCloud(series.getCoverImageUrl(), dto.getImage());
    }

    public SeriesDetailDTO getSeriesInfo(long seriesId){
        Series series = seriesRepository.findById(seriesId).orElseThrow(() -> new CustomBadRequestException(400, "찾을수 없는 series"));
        SeriesDetailDTO dto = modelMapper.map(series, SeriesDetailDTO.class);
        dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
        Set<String> tagNames = series.getTags().stream()
                .map(seriesTag -> seriesTag.getTag().getName())
                .collect(Collectors.toSet());
        dto.setTags(tagNames);
        return dto;
    }


    @Transactional
    public void deleteSeries(long seriesId){
        seriesRepository.deleteById(seriesId);
    }

    @Transactional
    public void updateSeries(SeriesModifyDTO dto){
        Series series = seriesRepository.findById(dto.getId()).get();

        Set<Tag> tagEntities = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .collect(Collectors.toSet());

        series.updateSeries(dto.getTitle(),dto.getDescription(),dto.getType(),tagEntities,dto.getImage());
        seriesRepository.save(series);
        imageUploadService.uploadImageToCloud(series.getCoverImageUrl(), dto.getImage());
    }

    public boolean isOwner(long seriesId, long memberId){
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new CustomBadRequestException(404, "Series not found"));
        return series.getAuthorId() == memberId;
    }

    public List<SeriesPreviewDTO> getAllSeriesByBookMark(int index, int size, long memberId) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = bookMarkRepository.findSeriesByMemberId(memberId,pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }

    public List<SeriesPreviewDTO> getAllSeriesByLike(int index, int size, long memberId) {
        Pageable pageable = PageRequest.of(index, size);

        List<Series> seriesData = likeRepository.findSeriesByMemberId(memberId,pageable);
        List<SeriesPreviewDTO> data = new ArrayList<>();
        for (Series series : seriesData) {
            SeriesPreviewDTO dto = modelMapper.map(series, SeriesPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getAuthorId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }
}
