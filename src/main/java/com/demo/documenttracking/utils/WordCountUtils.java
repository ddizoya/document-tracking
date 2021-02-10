package com.demo.documenttracking.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

public class WordCountUtils {

    public static synchronized Map<String, Long> inputStreamCount(InputStream inputStream) {

        Map<String, Long> wordCountResult = Collections.<String, Long>emptyMap();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            Optional<String[]> splittedText = br
                    .lines()
                    .reduce((firstLine, secondLine) -> String.join(" ", firstLine, secondLine))
                    .map(text -> text.split(" "));

            if (splittedText.isPresent()) {
                wordCountResult = Arrays
                        .stream(splittedText.get())
                        .collect(Collectors.groupingBy(Function.identity(), counting()));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordCountResult;
    }

}
