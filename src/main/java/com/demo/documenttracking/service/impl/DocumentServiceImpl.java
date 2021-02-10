package com.demo.documenttracking.service.impl;

import com.demo.documenttracking.dto.DocumentDTO;
import com.demo.documenttracking.dto.UserDTO;
import com.demo.documenttracking.dto.WordCountDTO;
import com.demo.documenttracking.exception.DocumentStoringException;
import com.demo.documenttracking.exception.ResourceNotFoundException;
import com.demo.documenttracking.repository.DocumentRepository;
import com.demo.documenttracking.repository.TeamRepository;
import com.demo.documenttracking.repository.UserRepository;
import com.demo.documenttracking.repository.WordCountRepository;
import com.demo.documenttracking.repository.entity.Document;
import com.demo.documenttracking.repository.entity.Team;
import com.demo.documenttracking.repository.entity.User;
import com.demo.documenttracking.repository.entity.WordCount;
import com.demo.documenttracking.service.DocumentService;
import com.demo.documenttracking.utils.WordCountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private WordCountRepository wordCountRepository;

    @Override
    @Transactional
    public Optional<URI> saveDocument(String teamId, String userId, MultipartFile document) {

        Optional<URI> resourceResult = Optional.empty();

        User user = userRepository.findByEmailAndRegistrationsTeamName(userId, teamId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        log.debug("Found user {}", user);

        Document storedDocument = null;
        try {

            storedDocument = documentRepository.save(toDocument(document, user));
            storedDocument.addWordCounts(calculateWordCounts(document));

            log.debug("Stored document {}", storedDocument);

        } catch (IOException e) {
            throw new DocumentStoringException("Error saving document");
        }

        if (Optional.ofNullable(storedDocument).isPresent()) {

            resourceResult = Optional.of(ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/documents/{documentId}")
                    .buildAndExpand(storedDocument.getId())
                    .toUri());
        }
        return resourceResult;
    }

    @Override
    public List<DocumentDTO> list(String teamName, Integer page, Integer limit) {

        Team team = teamRepository.findById(teamName)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        log.debug("Found team {}", team);

        return listDocuments(team, page, limit);
    }

    private List<DocumentDTO> listDocuments(Team team, Integer page, Integer limit) {
        return documentRepository.findAllByUserRegistrationsTeam(team, PageRequest.of(page, limit))
                .stream()
                .map(document -> doDocumentDTO(document))
                .collect(Collectors.toList());
    }

    private DocumentDTO doDocumentDTO(Document document) {
        return DocumentDTO.builder()
                .id(document.getId())
                .name(document.getName())
                .uploadedAt(document.getUploadedDate())
                .words(document.getWordCounts().stream().count())
                .user(document.getUser().getEmail())
                .build();
    }

    @Override
    public List<UserDTO> listInactiveUsers(LocalDate from, LocalDate to, Integer page, Integer limit) {

        return userRepository
                .findInactiveUsers(from.atStartOfDay(), to.atStartOfDay(), PageRequest.of(page, limit))
                .stream()
                .map(user -> UserDTO.builder()
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    private Document toDocument(MultipartFile document, User user) throws IOException {

        Assert.notNull(document, "Cannot be null");

        return Document.newDocumentBuilder()
                .user(user)
                .data(document.getBytes())
                .name(document.getOriginalFilename())
                .uploadedDate(LocalDateTime.now())
                .build();
    }

    private List<WordCount> calculateWordCounts(MultipartFile document) throws IOException {

        Assert.notNull(document, "document cannot be null");

        return WordCountUtils.inputStreamCount(document.getInputStream())
                .entrySet()
                .stream()
                .map(entry -> WordCount.builder()
                        .word(entry.getKey())
                        .count(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<WordCountDTO> wordCount(Long documentId,
                                        List<String> wordExclusions,
                                        Integer page,
                                        Integer limit) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        List<String> exclusions = Optional.ofNullable(wordExclusions)
                .orElse(Collections.singletonList(""));

        return wordCountRepository.findAllByDocumentAndWordNotIn(document, exclusions, PageRequest.of(page, limit))
                .stream()
                .map(wc -> WordCountDTO.builder()
                        .word(wc.getWord())
                        .count(wc.getCount())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDTO find(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        return doDocumentDTO(document);
    }
}
