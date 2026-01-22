package com.zyad.wordle_backend.entity;

import com.zyad.wordle_backend.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_stats")
public class UserStats {

    @Id
    private Long userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int gamesPlayed;
    private int wins;
    private int currentStreak;
    private int maxStreak;

    private int winIn1;
    private int winIn2;
    private int winIn3;
    private int winIn4;
    private int winIn5;
    private int winIn6;

    private LocalDate lastPlayedDate;
    private LocalDate lastWinDate;

    public Long getUserId() { return userId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getGamesPlayed() { return gamesPlayed; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }

    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getMaxStreak() { return maxStreak; }
    public void setMaxStreak(int maxStreak) { this.maxStreak = maxStreak; }

    public int getWinIn1() { return winIn1; }
    public void setWinIn1(int winIn1) { this.winIn1 = winIn1; }

    public int getWinIn2() { return winIn2; }
    public void setWinIn2(int winIn2) { this.winIn2 = winIn2; }

    public int getWinIn3() { return winIn3; }
    public void setWinIn3(int winIn3) { this.winIn3 = winIn3; }

    public int getWinIn4() { return winIn4; }
    public void setWinIn4(int winIn4) { this.winIn4 = winIn4; }

    public int getWinIn5() { return winIn5; }
    public void setWinIn5(int winIn5) { this.winIn5 = winIn5; }

    public int getWinIn6() { return winIn6; }
    public void setWinIn6(int winIn6) { this.winIn6 = winIn6; }

    public LocalDate getLastPlayedDate() { return lastPlayedDate; }
    public void setLastPlayedDate(LocalDate lastPlayedDate) { this.lastPlayedDate = lastPlayedDate; }

    public LocalDate getLastWinDate() { return lastWinDate; }
    public void setLastWinDate(LocalDate lastWinDate) { this.lastWinDate = lastWinDate; }
}
