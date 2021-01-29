package com.goldcrestwilma.batch.job.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ScoreKeywords {

    @Id
    private String pid;

    private double score;

    private String keywords;

}
