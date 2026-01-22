package com.zyad.wordle_backend.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PuzzleService {

    private final List<String> answerWords = new ArrayList<>();
    private static final LocalDate EPOCH = LocalDate.of(2026, 1, 1);

    public PuzzleService() {
        loadWords("words/answer-words.txt");
        if (answerWords.isEmpty()) {
            throw new IllegalStateException("answer-words.txt is empty");
        }
    }

    private void loadWords(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase();
                if (word.matches("^[A-Z]{5}$")) {
                    answerWords.add(word);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load answers from " + path, e);
        }
    }

    public String getPuzzleId() {
        return LocalDate.now().toString();
    }

    public int getLength() {
        return 5;
    }

    public int getMaxAttempts() {
        return 6;
    }

    public String getAnswerForPuzzle(String puzzleId) {
        LocalDate date = LocalDate.parse(puzzleId);
        long dayIndex = Math.abs(date.toEpochDay() - EPOCH.toEpochDay());
        int idx = (int) (dayIndex % answerWords.size());
        return answerWords.get(idx);
    }
}
