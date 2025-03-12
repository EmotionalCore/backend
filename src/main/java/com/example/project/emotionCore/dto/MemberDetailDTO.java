package com.example.project.emotionCore.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDetailDTO {

    private Long id;

    private String username;

    private String email;

    private String profileImageUrl;

    private String description;

    private String links;

    private String tags;
}
