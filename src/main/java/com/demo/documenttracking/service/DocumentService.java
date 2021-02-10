package com.demo.documenttracking.service;

import com.demo.documenttracking.dto.DocumentDTO;
import com.demo.documenttracking.dto.UserDTO;
import com.demo.documenttracking.dto.WordCountDTO;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DocumentService {

    Optional<URI> saveDocument(String teamId, String userId, MultipartFile document);

    List<DocumentDTO> list(String teamId, Integer page, Integer size);

    List<UserDTO> listInactiveUsers(LocalDate from, LocalDate to, Integer page, Integer limit);

    List<WordCountDTO> wordCount(Long documentId,
                                 List<String> wordExclusions,
                                 Integer page,
                                 Integer limit);

    DocumentDTO find(Long documentId);
}
