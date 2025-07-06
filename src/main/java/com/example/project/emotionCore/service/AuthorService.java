package com.example.project.emotionCore.service;

import com.example.project.emotionCore.dto.AuthorPreviewDTO;
import com.example.project.emotionCore.repository.AuthorRepository;
import com.example.project.emotionCore.repository.EpisodeRepository;
import com.example.project.emotionCore.repository.MemberRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.domain.Tag;
import com.example.project.emotionCore.dto.AuthorDTO;
import com.example.project.emotionCore.dto.MyPageUpdateDTO;
import com.example.project.emotionCore.dto.NewAuthorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final MemberRepository memberRepository;
    private final EpisodeRepository episodeRepository;
    private final AuthorRepository authorRepository;
    private final TagService tagService;
    ModelMapper modelMapper = new ModelMapper();


    public AuthorService(AuthorRepository authorRepository, MemberRepository memberRepository, EpisodeRepository episodeRepository,  TagService tagService) {
        this.authorRepository = authorRepository;
        this.memberRepository = memberRepository;
        this.episodeRepository = episodeRepository;
        this.tagService = tagService;
    }

    public List<AuthorDTO> getAllByKeywords(List<String> keywords) {
        List<Author> entity = authorRepository.findByKeywords(keywords);
        List<AuthorDTO> data = new ArrayList<>();
        for (Author author : entity) {
            AuthorDTO dto = modelMapper.map(author, AuthorDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getId()).get().getUsername());
            dto.setProfileImageUrl(memberRepository.findById((long) dto.getId()).get().getProfileImageUrl());

            Set<String> tagNames = author.getTags().stream()
                    .map(authorTag -> authorTag.getTag().getName())
                    .collect(Collectors.toSet());
            dto.setTags(tagNames);

            data.add(dto);
        }
        return data;
    }

    public Author getAuthorByMemberId(long memberId) {
        Author author = authorRepository.findById(memberId);
        if(author == null) {
            throw new EntityNotFoundException("없는 작가입니다."+memberId);
        }
        return author;
    }


    public void updateAuthor(long id, MyPageUpdateDTO dto) {
        Author author = authorRepository.findById(id);
        Set<Tag> tagEntities = dto.getTags().stream()
                .map(tagService::findOrCreateByName)
                .collect(Collectors.toSet());

        author.updateAuthor(dto.getDescription(), dto.getLinks(), tagEntities);
        authorRepository.save(author);  // JPA의 save 메서드는 업데이트도 처리합니다.
    }

    public List<NewAuthorDTO> getNewAuthors() {
        Pageable top5 = PageRequest.of(0, 5);
        return episodeRepository.findNewAuthors(top5);
    }

    public List<AuthorPreviewDTO> getNewAuthors(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Author> authorsData = authorRepository.findBySeriesListIsNotEmptyOrderByIdDesc(pageable);
        List<AuthorPreviewDTO> data = new ArrayList<>();

        for (Author author :authorsData) {
            AuthorPreviewDTO dto = modelMapper.map(author, AuthorPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getId()).get().getUsername());
            dto.setCoverImageUrl(memberRepository.findById((long) dto.getId()).get().getProfileImageUrl());
            data.add(dto);
        }
        return data;
    }


    public List<AuthorPreviewDTO> getMonthlyBestAuthor(int limit) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(30);
        List<Author> authorsData = authorRepository.findByMonthlyBestAuthor(today,nextMonth,limit);
        List<AuthorPreviewDTO> data = new ArrayList<>();
        for (Author authors : authorsData) {
            AuthorPreviewDTO dto = modelMapper.map(authors, AuthorPreviewDTO.class);
            dto.setAuthorName(memberRepository.findById((long) dto.getId()).get().getUsername());
            dto.setCoverImageUrl(memberRepository.findById((long) dto.getId()).get().getProfileImageUrl());
            data.add(dto);
        }
        return data;
    }
}
