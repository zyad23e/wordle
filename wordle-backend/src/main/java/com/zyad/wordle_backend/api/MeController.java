package com.zyad.wordle_backend.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(Authentication auth) {
        if (auth == null) {
            return Map.of(
                    "loggedIn", false
            );
        }

        return Map.of(
                "loggedIn", true,
                "username", auth.getName()
        );
    }
}
