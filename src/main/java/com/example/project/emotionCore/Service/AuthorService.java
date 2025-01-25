package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.AuthorRepository;
import com.example.project.emotionCore.domain.Author;
import com.example.project.emotionCore.dto.AuthorDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    AuthorRepository authorRepository;
    ModelMapper modelMapper = new ModelMapper();
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    public List<AuthorDTO> getAllByKeywords(List<String> keywords) {
        List<Author> entity = authorRepository.findByKeywords(keywords);
        List<AuthorDTO> data = new ArrayList<>();
        for (Author author : entity) {
            data.add(modelMapper.map(author, AuthorDTO.class));
        }
        return data;
    }
}
