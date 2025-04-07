package com.example.project.emotionCore.dto;

import lombok.*;

@Builder
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    private Long id;
    private String name;
}
