package com.zyad.wordle_backend.domain;

import java.util.ArrayList;
import java.util.List;

public class GuessEvaluator {

    public List<LetterStatus> evaluate(String answer, String guess) {
        answer = answer.toUpperCase();
        guess = guess.toUpperCase();

        int length = answer.length();
        List<LetterStatus> result = new ArrayList<>(length);

        int[] remainingLetters = new int[26];

        for (int i = 0; i < length; i++) {
            char answerChar = answer.charAt(i);
            char guessChar = guess.charAt(i);

            if (guessChar == answerChar) {
                result.add(LetterStatus.CORRECT);
            } else {
                result.add(null);
                remainingLetters[answerChar - 'A']++;
            }
        }

        for (int i = 0; i < length; i++) {
            if (result.get(i) != null) {
                continue;
            }

            char guessChar = guess.charAt(i);
            int index = guessChar - 'A';

            if (index >= 0 && index < 26 && remainingLetters[index] > 0) {
                result.set(i, LetterStatus.PRESENT);
                remainingLetters[index]--;
            } else {
                result.set(i, LetterStatus.ABSENT);
            }
        }

        return result;
    }
}
