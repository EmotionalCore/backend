package com.example.project.emotionCore.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyPageUpdateDTO {

    //Member 쪽입니당.
    private String username;
    private String email;
    private MultipartFile profileImage;

    //Author 쪽
    private String description;
    private String links;
    private Set<String> tags;

}
