package com.example.project.emotionCore.service;

import com.example.project.emotionCore.dto.SearchWorkDTO;
import com.example.project.emotionCore.repository.SearchWorkRepository;
import com.example.project.emotionCore.domain.SearchWork;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SearchWorkService {

    private final SearchWorkRepository searchWorkRepository;
    ModelMapper modelMapper = new ModelMapper();

    @Transactional
    public void processSearch(String searchWord) {
        SearchWork searchWork = searchWorkRepository.findBySearchWord(searchWord)
                .orElseGet(() -> {
                    // 없다면 새로 생성
                    SearchWork newSearchWork = new SearchWork(searchWord);
                    return searchWorkRepository.save(newSearchWork);
                });

        // 검색 횟수 증가
        searchWork.incrementSearchCount();
        searchWorkRepository.save(searchWork);
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
