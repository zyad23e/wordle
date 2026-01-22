package com.zyad.wordle_backend.api.dto;

import com.zyad.wordle_backend.domain.LetterStatus;
import java.util.List;

public record GuessResponse(
        boolean isValidWord,
        boolean isWin,
        boolean isGameOver,
        int attempt,
        List<LetterStatus> statuses
) {}
