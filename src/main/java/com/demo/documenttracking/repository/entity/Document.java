package com.demo.documenttracking.repository.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Document implements Serializable {

    @Builder(builderMethodName = "newDocumentBuilder")
    public Document(String name, byte[] data, LocalDateTime uploadedDate, User user) {
        this.name = name;
        this.data = data;
        this.uploadedDate = uploadedDate;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] data;

    private LocalDateTime uploadedDate;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "document", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<WordCount> wordCounts = new ArrayList<>();

    public void addWordCounts(List<WordCount> wordCounts) {
        wordCounts.forEach(wc -> {
            wc.setDocument(this);
            this.wordCounts.add(wc);
        });
    }
}
