package com.zyad.wordle_backend.api.dto;

public record PuzzleResponse(String puzzleId, int length, int maxAttempts) {}
