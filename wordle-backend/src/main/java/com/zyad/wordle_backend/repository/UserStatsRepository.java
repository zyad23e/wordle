package com.zyad.wordle_backend.repository;

import com.zyad.wordle_backend.entity.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {}
