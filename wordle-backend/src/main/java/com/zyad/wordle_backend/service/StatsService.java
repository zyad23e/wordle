package com.zyad.wordle_backend.service;


import com.zyad.wordle_backend.entity.User;
import com.zyad.wordle_backend.entity.UserStats;
import com.zyad.wordle_backend.repository.UserRepository;
import com.zyad.wordle_backend.repository.UserStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class StatsService {

    private final UserRepository users;
    private final UserStatsRepository statsRepo;

    public StatsService(UserRepository users, UserStatsRepository statsRepo) {
        this.users = users;
        this.statsRepo = statsRepo;
    }

    @Transactional
    public void recordGameEnd(String username, boolean isWin, int attempt, LocalDate today) {

        User user = users.findByUsername(username).orElseThrow();
        UserStats stats = statsRepo.findById(user.getId()).orElseThrow();

        if (today.equals(stats.getLastPlayedDate())) {
            return;
        }

        stats.setLastPlayedDate(today);
        stats.setGamesPlayed(stats.getGamesPlayed() + 1);

        if (isWin) {
            stats.setWins(stats.getWins() + 1);

            LocalDate lastWin = stats.getLastWinDate();
            if (lastWin != null && lastWin.plusDays(1).equals(today)) {
                stats.setCurrentStreak(stats.getCurrentStreak() + 1);
            } else {
                stats.setCurrentStreak(1);
            }

            stats.setLastWinDate(today);
            stats.setMaxStreak(Math.max(stats.getMaxStreak(), stats.getCurrentStreak()));

            switch (attempt + 1) {
                case 1 -> stats.setWinIn1(stats.getWinIn1() + 1);
                case 2 -> stats.setWinIn2(stats.getWinIn2() + 1);
                case 3 -> stats.setWinIn3(stats.getWinIn3() + 1);
                case 4 -> stats.setWinIn4(stats.getWinIn4() + 1);
                case 5 -> stats.setWinIn5(stats.getWinIn5() + 1);
                case 6 -> stats.setWinIn6(stats.getWinIn6() + 1);
            }
        } else {
            stats.setCurrentStreak(0);
        }
    }
}
