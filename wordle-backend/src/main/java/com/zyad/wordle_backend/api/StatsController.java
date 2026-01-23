package com.zyad.wordle_backend.api;

import com.zyad.wordle_backend.entity.User;
import com.zyad.wordle_backend.entity.UserStats;
import com.zyad.wordle_backend.repository.UserRepository;
import com.zyad.wordle_backend.repository.UserStatsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final UserRepository users;
    private final UserStatsRepository statsRepo;

    public StatsController(UserRepository users, UserStatsRepository statsRepo) {
        this.users = users;
        this.statsRepo = statsRepo;
    }

    public record StatsResponse(
            int gamesPlayed,
            int wins,
            int currentStreak,
            int maxStreak,
            int winIn1,
            int winIn2,
            int winIn3,
            int winIn4,
            int winIn5,
            int winIn6
    ) {}

    @GetMapping
    public StatsResponse getStats(Authentication auth) {
        if (auth == null) {
            return new StatsResponse(0,0,0,0,0,0,0,0,0,0);
        }

        User user = users.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));

        UserStats s = statsRepo.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Stats not found for userId: " + user.getId()));

        return new StatsResponse(
                s.getGamesPlayed(),
                s.getWins(),
                s.getCurrentStreak(),
                s.getMaxStreak(),
                s.getWinIn1(),
                s.getWinIn2(),
                s.getWinIn3(),
                s.getWinIn4(),
                s.getWinIn5(),
                s.getWinIn6()
        );
    }

}
