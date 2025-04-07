package com.example.project.emotionCore.domain;


import com.example.project.emotionCore.dto.MyPageUpdateDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="authors")
public class Author{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // 데이터베이스 상 관리하는 고객 번호

    @Column(name = "description")
    private String description;

    @Column(name = "links")
    private String links;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AuthorTag> tags = new HashSet<>();

    //나중에 더 추가
    @OneToMany(mappedBy = "authorInfos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Series> seriesList = new ArrayList<>();

    public Author(String description, String links, Set<Tag> tags) {
        this.description = description;
        this.links = links;
        for (Tag tag : tags) {
            AuthorTag authorTag = AuthorTag.builder()
                    .author(this)
                    .tag(tag)
                    .build();
            this.tags.add(authorTag);
        }
    }

    public void updateAuthor(String description, String links, Set<Tag> tags) {
        this.description = description;
        this.links = links;
        this.tags.clear();

        for (Tag tag : tags) {
            AuthorTag authorTag = AuthorTag.builder()
                    .author(this)
                    .tag(tag)
                    .build();
            this.tags.add(authorTag);
        }
    }
}
