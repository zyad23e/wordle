package com.zyad.wordle_backend.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Service
public class DictionaryService {

    private final Set<String> validWords = new HashSet<>();

    public DictionaryService() {
        loadWords("words/valid-words.txt");
    }

    private void loadWords(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)
        )) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toUpperCase();
                if (word.matches("^[A-Z]{5}$")) {
                    validWords.add(word);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load dictionary from " + path, e);
        }
    }

    public boolean isValid(String guess) {
        if (guess == null) return false;
        return validWords.contains(guess.trim().toUpperCase());
    }
}
