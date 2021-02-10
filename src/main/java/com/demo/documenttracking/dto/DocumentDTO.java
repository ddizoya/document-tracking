package com.demo.documenttracking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {

    private Long id;
    private String name;
    private Long words;
    private String user;
    private LocalDateTime uploadedAt;
}
