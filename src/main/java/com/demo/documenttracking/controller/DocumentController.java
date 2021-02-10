package com.demo.documenttracking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "Document API")
public interface DocumentController {

    @ApiOperation(value = "Creates a new document")
    ResponseEntity createDocument(@ApiParam(value = "Team identifier", required = true) String teamId,
                                  @ApiParam(value = "User identifier", required = true) String userId,
                                  @ApiParam(value = "Document to register.", required = true) MultipartFile document);

    @ApiOperation(value = "List all documents by team name")
    ResponseEntity listDocuments(@ApiParam(value = "Team identifier", required = true) String teamId,
                                 @ApiParam(value = "Page", defaultValue = "0") Integer page,
                                 @ApiParam(value = "Limit", defaultValue = "10") Integer limit);

    @ApiOperation(value = "List all inactive users who doesn't uploaded any documents ")
    ResponseEntity listInactiveUsers(@ApiParam(value = "Starting date", required = true, example = "2020-01-01") LocalDate from,
                                     @ApiParam(value = "Ending date", required = true, example = "2020-01-01") LocalDate to,
                                     @ApiParam(value = "Page", defaultValue = "0") Integer page,
                                     @ApiParam(value = "Limit", defaultValue = "10") Integer limit);

    @ApiOperation(value = "Gets a document")
    ResponseEntity docWordCount(@ApiParam(value = "Document Id", required = true) Long documentId);

    @ApiOperation(value = "Prints wordcount for a given document")
    ResponseEntity docWordCount(@ApiParam(value = "Document Id", required = true) Long documentId,
                                @ApiParam(value = "List of excluded words") List<String> wordExclusions,
                                @ApiParam(value = "Page", defaultValue = "0") Integer page,
                                @ApiParam(value = "Limit", defaultValue = "10") Integer limit);
}
