package com.demo.documenttracking.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class WordCount implements Serializable {

    @Builder
    public WordCount(String word, Long count) {
        this.word = word;
        this.count = count;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;

    private Long count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Document document;
}
