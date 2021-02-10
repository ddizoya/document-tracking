package com.demo.documenttracking.repository;

import com.demo.documenttracking.repository.entity.Document;
import com.demo.documenttracking.repository.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Page<Document> findAllByUserRegistrationsTeam(Team team, Pageable pageable);

    Page<Document> findByUploadedDateBeforeAndUploadedDateAfter(LocalDate from, LocalDate to, PageRequest of);
}

