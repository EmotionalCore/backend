package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.SearchWorkRepository;
import com.example.project.emotionCore.domain.SearchWork;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SearchWorkService {

    private final SearchWorkRepository searchWorkRepository;

    @Transactional
    public void processSearch(String searchWord) {
        // searchWord에 해당하는 엔티티를 검색
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

}
