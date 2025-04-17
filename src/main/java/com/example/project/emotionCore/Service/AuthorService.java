package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.AuthorRepository;
import com.example.project.emotionCore.Repository.EpisodeRepository;
import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.Repository.TagRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.domain.Tag;
import com.example.project.emotionCore.dto.AuthorDTO;
import com.example.project.emotionCore.dto.MyPageUpdateDTO;
import com.example.project.emotionCore.dto.NewAuthorDTO;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final MemberRepository memberRepository;
    private final EpisodeRepository episodeRepository;

    private final AuthorRepository authorRepository;
    private final TagService tagService;
    ModelMapper modelMapper = new ModelMapper();


    public AuthorService(AuthorRepository authorRepository, MemberRepository memberRepository, EpisodeRepository episodeRepository,  TagService tagService) {{
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

}
