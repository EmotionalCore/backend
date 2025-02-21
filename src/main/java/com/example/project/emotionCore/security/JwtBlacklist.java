package com.example.project.emotionCore.security;

import com.example.project.emotionCore.dto.JwtTokenDTO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtBlacklist {
    //나중에 시간 지난 블랙리스트 지우는거 어떻게 만들까
    private final Set<String> blacklist = new HashSet<>();

    public void addBlacklist(String token) {
        blacklist.add(token);
    }
    public void addBlacklist(JwtTokenDTO jwtTokenDTO) {
        blacklist.add(jwtTokenDTO.getAccessToken());
        blacklist.add(jwtTokenDTO.getRefreshToken());
    }
    public void removeBlacklist(String token) {
        blacklist.remove(token);
    }
    public boolean isBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
