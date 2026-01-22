package com.zyad.wordle_backend.api;

import com.zyad.wordle_backend.api.dto.GuessRequest;
import com.zyad.wordle_backend.api.dto.GuessResponse;
import com.zyad.wordle_backend.api.dto.PuzzleResponse;
import com.zyad.wordle_backend.domain.GuessEvaluator;
import com.zyad.wordle_backend.domain.LetterStatus;
import com.zyad.wordle_backend.service.DictionaryService;
import com.zyad.wordle_backend.service.PuzzleService;
import com.zyad.wordle_backend.service.StatsService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PuzzleController {

    private final DictionaryService dictionaryService;
    private final PuzzleService puzzleService;
    private final StatsService statsService;

    public PuzzleController(
            DictionaryService dictionaryService,
            PuzzleService puzzleService,
            StatsService statsService
    ) {
        this.dictionaryService = dictionaryService;
        this.puzzleService = puzzleService;
        this.statsService = statsService;
    }

    @GetMapping("/puzzle")
    public PuzzleResponse getPuzzle() {
        return new PuzzleResponse(
                puzzleService.getPuzzleId(),
                puzzleService.getLength(),
                puzzleService.getMaxAttempts()
        );
    }

    @PostMapping("/guess")
    public GuessResponse submitGuess(
            @Valid @RequestBody GuessRequest request,
            Authentication auth
    ) {
        int attempt = request.attempt();
        String guess = request.guess().toUpperCase();

        if (!dictionaryService.isValid(guess)) {
            return new GuessResponse(false, false, false, attempt, List.of());
        }

        String answer = puzzleService.getAnswerForPuzzle(request.puzzleId());

        GuessEvaluator evaluator = new GuessEvaluator();
        List<LetterStatus> statuses = evaluator.evaluate(answer, guess);

        boolean isWin = statuses.stream().allMatch(s -> s == LetterStatus.CORRECT);

        int maxAttempts = puzzleService.getMaxAttempts();
        boolean isGameOver = isWin || (attempt + 1 >= maxAttempts);

        if (auth != null && isGameOver) {
            statsService.recordGameEnd(
                    auth.getName(),
                    isWin,
                    attempt,
                    LocalDate.now()
            );
        }

        return new GuessResponse(true, isWin, isGameOver, attempt, statuses);
    }
}
