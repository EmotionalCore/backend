package com.example.project.emotionCore.service;

import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.SeriesPreviewDTO;
import com.example.project.emotionCore.repository.BookMarkRepository;
import com.example.project.emotionCore.repository.CommentRepository;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.repository.LikeRepository;
import com.example.project.emotionCore.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MineStorageService {

    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public List<Episode> getEpisodesByMember(Member member) {
        List<Comment> comments = commentRepository.findByMember(member);
        return comments.stream()
                .map(Comment::getEpisode)
                .distinct()
                .collect(Collectors.toList());
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


    public int getBookmarkedSeriesCount(Long memberId){
        return bookMarkRepository.countByMemberId(memberId);
    }

    public int getLikedSeriesCount(Long memberId) {
        return likeRepository.countByMemberId(memberId);
    }
}
