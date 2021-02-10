package com.demo.documenttracking.controller.impl;

import com.demo.documenttracking.controller.DocumentController;
import com.demo.documenttracking.dto.DocumentDTO;
import com.demo.documenttracking.service.DocumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class DocumentControllerImpl implements DocumentController {

    @Autowired
    private DocumentService documentService;

    @Override
    @PostMapping("/teams/{teamId}/users/{userId}/documents")
    public ResponseEntity createDocument(@PathVariable @NotBlank String teamId,
                                         @PathVariable @NotBlank @Email String userId,
                                         @RequestParam("document") @NotNull MultipartFile document) {


        Optional<URI> documentResource = documentService.saveDocument(teamId, userId, document);

        ResponseEntity responseEntity = ResponseEntity.unprocessableEntity().build();

        if (documentResource.isPresent()) {
            return ResponseEntity.created(documentResource.get()).build();
        }
        return responseEntity;

    }

    @Override
    @GetMapping("/teams/{teamId}/documents")
    public ResponseEntity<List<DocumentDTO>> listDocuments(@PathVariable @NotBlank String teamId,
                                                           @RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "10") Integer limit) {

        List documents = documentService.list(teamId, page, limit);
        return ResponseEntity.ok().body(documents);

    }

    @Override
    @GetMapping("/documents/users/status/inactive")
    public ResponseEntity listInactiveUsers(@RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                            @RequestParam(defaultValue = "0") Integer page,
                                            @RequestParam(defaultValue = "10") Integer limit) {

        List documents = documentService.listInactiveUsers(from, to, page, limit);
        return ResponseEntity.ok().body(documents);
    }

    @Override
    @GetMapping("/documents/{documentId}")
    public ResponseEntity docWordCount(@PathVariable @NotBlank Long documentId) {

        DocumentDTO document = documentService.find(documentId);
        return ResponseEntity.ok().body(document);
    }

    @Override
    @GetMapping("/documents/{documentId}/word-count")
    public ResponseEntity docWordCount(@PathVariable @NotBlank Long documentId,
                                       @RequestParam(required = false) List<String> wordExclusions,
                                       @RequestParam(defaultValue = "0") Integer page,
                                       @RequestParam(defaultValue = "10") Integer limit) {

        List documents = documentService.wordCount(documentId, wordExclusions, page, limit);
        return ResponseEntity.ok().body(documents);
    }

}
