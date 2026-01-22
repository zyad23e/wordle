package com.zyad.wordle_backend.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record GuessRequest(
        @NotBlank String puzzleId,

        @NotBlank
        @Pattern(regexp = "^[a-zA-Z]{5}$", message = "guess must be exactly 5 letters")
        String guess,

        @NotNull
        @Min(0)
        Integer attempt
) {}
