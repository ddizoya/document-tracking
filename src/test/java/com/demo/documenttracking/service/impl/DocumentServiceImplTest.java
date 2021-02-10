package com.demo.documenttracking.service.impl;

import com.demo.documenttracking.dto.DocumentDTO;
import com.demo.documenttracking.exception.ResourceNotFoundException;
import com.demo.documenttracking.repository.DocumentRepository;
import com.demo.documenttracking.repository.TeamRepository;
import com.demo.documenttracking.repository.UserRepository;
import com.demo.documenttracking.repository.entity.Document;
import com.demo.documenttracking.repository.entity.Team;
import com.demo.documenttracking.repository.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    private PodamFactory podam = new PodamFactoryImpl();

    @DisplayName("Given a doc, when saved, then we get a URI with the resrouce created")
    @Test
    void saveDocument_WhenTeamAndUserAreValid_ThenIsSaved() {

        //Given

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(documentRepository.save(any(Document.class))).thenReturn(new Document());
        when(userRepository.findByEmailAndRegistrationsTeamName(anyString(), anyString())).thenReturn(Optional.of(new User()));
        MockMultipartFile uploadFile = new MockMultipartFile("some-file.txt.txt", "hello\nhworld".getBytes());

        //When
        Optional<URI> resourceUri = documentService.saveDocument("teamId", "", uploadFile);

        //Then
        assertTrue(resourceUri.isPresent());
        verify(documentRepository).save(any(Document.class));
    }


    @DisplayName("Given a doc, when user is not found for a given email and team name, then an exception is raised")
    @Test
    void saveDocument_WhenUserNotFound_ThenException() {

        //Given
        MockMultipartFile uploadFile = new MockMultipartFile("some-file.txt.txt", "hello\nhworld".getBytes());

        //When && Then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            documentService.saveDocument("teamId", "", uploadFile);
        });
    }

    @DisplayName("Given a team name, when listed by team name, then data is retrieved")
    @Test
    void list_WhenDocumentsListedByTeamName_ThenDocumentsRetrieved() {

        //Given
        Document mockedDoc = Document.newDocumentBuilder()
                .data("dummy-text".getBytes())
                .name("dummy")
                .uploadedDate(LocalDateTime.now())
                .user(new User())
                .build();

        when(documentRepository.findAllByUserRegistrationsTeam(any(Team.class), any(Pageable.class))).thenReturn(new PageImpl<>(Arrays.asList(mockedDoc)));
        when(teamRepository.findById(anyString())).thenReturn(Optional.of(new Team()));

        //When
        List<DocumentDTO> documents = documentService.list("teamId", 0, 10);
        DocumentDTO resultDto = documents.stream().findFirst().orElse(new DocumentDTO());

        //Then
        assertNotNull(documents);
        assertFalse(documents.isEmpty());
        assertEquals(mockedDoc.getUser().getEmail(), resultDto.getUser());
        assertEquals(mockedDoc.getId(), resultDto.getId());
        assertEquals(mockedDoc.getName(), resultDto.getName());
        assertEquals(mockedDoc.getUploadedDate(), resultDto.getUploadedAt());
        assertEquals(mockedDoc.getWordCounts().size(), resultDto.getWords());

        verify(documentRepository).findAllByUserRegistrationsTeam(any(Team.class), any(Pageable.class));
    }

    @DisplayName("When listing all documents by team name, if team is not found in DB, then a ResourceNotFoundException is raised ")
    @Test
    void list_WhenTeamNotFound_ThenExeptionRaised() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            documentService.list("teamId", 0, 10);
        });
    }


    @DisplayName("When counting a non existing document, an exception is raised ")
    @Test
    void list_WhenDocumentNotFound_ThenExeptionRaised() {

        //Given
        when(documentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            documentService.wordCount(1L, null, 0, 10);
        });
    }

}