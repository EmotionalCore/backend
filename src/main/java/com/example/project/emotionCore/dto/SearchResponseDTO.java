package com.example.project.emotionCore.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponseDTO {
    List<SeriesDetailDTO> seriesDetailDTOList;
    List<AuthorDTO> authorDTOList;
}
