package com.example.project.emotionCore.Service;

import com.example.project.emotionCore.Repository.CommentRepository;
import com.example.project.emotionCore.domain.Comment;
import com.example.project.emotionCore.domain.Episode;
import com.example.project.emotionCore.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MineStorageService {

    private final CommentRepository commentRepository;

    public List<Episode> getEpisodesByMember(Member member) {
        List<Comment> comments = commentRepository.findByMember(member);
        return comments.stream()
                .map(Comment::getEpisode)
                .distinct()
                .collect(Collectors.toList());
    }
}
