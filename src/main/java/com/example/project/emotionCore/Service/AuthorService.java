package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.AuthorRepository;
import com.example.project.emotionCore.Repository.MemberRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.dto.AuthorDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final MemberRepository memberRepository;
    AuthorRepository authorRepository;
    ModelMapper modelMapper = new ModelMapper();
    public AuthorService(AuthorRepository authorRepository, MemberRepository memberRepository) {
        this.authorRepository = authorRepository;
        this.memberRepository = memberRepository;
    }
    public List<AuthorDTO> getAllByKeywords(List<String> keywords) {
        List<Author> entity = authorRepository.findByKeywords(keywords);
        List<AuthorDTO> data = new ArrayList<>();
        for (Author author : entity) {
            AuthorDTO dto = modelMapper.map(author, AuthorDTO.class);
            dto.setName(memberRepository.findById((long) dto.getId()).get().getUsername());
            data.add(dto);
        }
        return data;
    }
}
