package com.zyad.wordle_backend.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuessEvaluatorTest {

    private final GuessEvaluator evaluator = new GuessEvaluator();

    @Test
    void marksAllCorrectWhenGuessMatchesAnswer() {
        List<LetterStatus> statuses = evaluator.evaluate("CRANE", "CRANE");

        assertEquals(List.of(
                LetterStatus.CORRECT,
                LetterStatus.CORRECT,
                LetterStatus.CORRECT,
                LetterStatus.CORRECT,
                LetterStatus.CORRECT
        ), statuses);
    }

    @Test
    void marksAbsentWhenLetterNotInAnswer() {
        List<LetterStatus> statuses = evaluator.evaluate("CRANE", "BULKY");

        assertTrue(
                statuses.stream().allMatch(status -> status == LetterStatus.ABSENT)
        );
    }

    @Test
    void handlesDuplicateLettersCorrectly() {
        // Answer has one 'L'. Guess has two 'L's.
        List<LetterStatus> statuses = evaluator.evaluate("ALERT", "LEVEL");

        assertEquals(5, statuses.size());
        assertTrue(statuses.contains(LetterStatus.ABSENT));
    }
}
