package com.demo.documenttracking.repository;

import com.demo.documenttracking.repository.entity.Document;
import com.demo.documenttracking.repository.entity.WordCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordCountRepository extends JpaRepository<WordCount, Long> {

    Page<WordCount> findAllByDocumentAndWordNotIn(Document document, List<String> exclusions, Pageable pageable);

}
