package com.zyad.wordle_backend.api;

import com.zyad.wordle_backend.service.PuzzleService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final PuzzleService puzzleService;

    public DevController(PuzzleService puzzleService) {
        this.puzzleService = puzzleService;
    }

    @GetMapping("/answer")
    public String revealTodayAnswer() {
        String puzzleId = puzzleService.getPuzzleId();
        return puzzleService.getAnswerForPuzzle(puzzleId);
    }
}

