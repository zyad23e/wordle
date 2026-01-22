package com.zyad.wordle_backend.api;

import com.zyad.wordle_backend.entity.User;
import com.zyad.wordle_backend.entity.UserStats;
import com.zyad.wordle_backend.repository.UserRepository;
import com.zyad.wordle_backend.repository.UserStatsRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final UserStatsRepository statsRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public AuthController(
            UserRepository users,
            UserStatsRepository statsRepo,
            PasswordEncoder encoder,
            AuthenticationManager authManager
    ) {
        this.users = users;
        this.statsRepo = statsRepo;
        this.encoder = encoder;
        this.authManager = authManager;
    }

    public record RegisterRequest(
            @NotBlank
            @Size(min = 3, max = 50)
            String username,

            @NotBlank
            @Size(min = 6, max = 100)
            String password
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {

        if (users.existsByUsername(req.username())) {
            return ResponseEntity.status(409).body("username_taken");
        }

        User user = new User();
        user.setUsername(req.username());
        user.setPasswordHash(encoder.encode(req.password()));
        user = users.save(user);

        UserStats stats = new UserStats();
        stats.setUser(user);
        statsRepo.save(stats);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);

        var session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", context);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
