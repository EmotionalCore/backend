package com.example.project.emotionCore.service;

import com.example.project.emotionCore.repository.BookMarkRepository;
import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.repository.SeriesRepository;
import com.example.project.emotionCore.domain.BookMark;
import com.example.project.emotionCore.domain.Member;
import com.example.project.emotionCore.domain.Series;
import com.example.project.emotionCore.dto.BookMarkRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final SeriesRepository seriesRepository;

    @Transactional
    public void toggleBookMark(Long id, BookMarkRequestDTO bookMarkRequestDTO) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("멤버 id를 찾을 수 없습니다."));

        BookMark bookmark = null;

        if (bookMarkRequestDTO.getSeriesId() != null) {
            Series series = seriesRepository.findById(bookMarkRequestDTO.getSeriesId())
                    .orElseThrow(() -> new EntityNotFoundException("시리즈 id를 찾을 수 없습니다."));

            bookmark = bookMarkRepository.findByMemberIdAndSeriesId(id, (long) series.getId()).orElse(null);

            if (bookmark != null) {
                bookMarkRepository.delete(bookmark);
                seriesRepository.subBookMarkCount(series);
            } else {
                bookMarkRepository.save(bookmark.builder()
                        .series(series)
                        .member(member)
                        .build());
                seriesRepository.addBookMarkCount(series);
            }
        }
    }
}
