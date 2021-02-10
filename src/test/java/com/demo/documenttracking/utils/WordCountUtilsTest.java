package com.demo.documenttracking.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class WordCountUtilsTest {

    @DisplayName("Given some input streams with string data, when counted, we get a map with word frequencies count")
    @ParameterizedTest
    @MethodSource("provideInputStreamingSamples")
    public void GivenAnInputStream_WhenProcessed_ThenWordsAreCounted(InputStream inpputStream, Map<String, Long> expected) {

        Assertions.assertTrue(WordCountUtils.inputStreamCount(inpputStream).equals(expected));
    }

    private static Stream<Arguments> provideInputStreamingSamples() throws IOException {

        return Stream.of(
                Arguments.of(new MockMultipartFile("sample", "one two three".getBytes()).getInputStream(),
                        new HashMap<String, Long>() {{
                            put("one", 1L);
                            put("two", 1L);
                            put("three", 1L);
                        }}),
                Arguments.of(new MockMultipartFile("sample", "one one one".getBytes()).getInputStream(),
                        new HashMap<String, Long>() {{
                            put("one", 3L);
                        }}),
                Arguments.of(new MockMultipartFile("sample", "one\none".getBytes()).getInputStream(),
                        new HashMap<String, Long>() {{
                            put("one", 2L);
                        }}),
                Arguments.of(new MockMultipartFile("sample", "".getBytes()).getInputStream(), Collections.emptyMap())
        );
    }

}