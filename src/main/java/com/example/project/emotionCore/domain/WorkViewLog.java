package com.example.project.emotionCore.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_view_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(WorkViewLog.viewId.class)
public class WorkViewLog {
    @Id
    @Column(name = "member_id")
    private long memberId;

    @Id
    @Column(name = "series_id")
    private long seriesId;

    @Id
    @Column(name = "episode_number")
    private long episodeNumber;

    @Column(name = "view_time")
    @Builder.Default
    private LocalDateTime viewTime = LocalDateTime.now();

    public void updateEpisodeNumber(long episodeNumber) {
        this.episodeNumber = episodeNumber;
    }


    @AllArgsConstructor
    @NoArgsConstructor
    public static class viewId implements Serializable {
        //이게 맞나? 그냥 id 하나 만들어주는게 낫나?
        private long memberId;
        private long seriesId;
        private long episodeNumber;
    }
}
