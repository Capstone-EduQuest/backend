package com.eduquest.backend.presentation.identity.controller;

import com.eduquest.backend.application.identity.service.AuthService;
import com.eduquest.backend.presentation.identity.dto.request.FindIdRequest;
import com.eduquest.backend.presentation.identity.dto.request.FindPasswordRequest;
import com.eduquest.backend.presentation.identity.dto.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("Sign In");
    }

    @PostMapping("/auth/find-id")
    public ResponseEntity<String> findId(@Valid @RequestBody FindIdRequest request) {

        authService.sendFindIdEmail(request.email());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/auth/find-password")
    public ResponseEntity<String> findPassword(@Valid @RequestBody FindPasswordRequest request) {

        authService.sendPasswordRestEmail(request.email(), request.userId());

        return ResponseEntity.status(204).build();
    }

    @PutMapping("/auth/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request.token(), request.newPassword());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        authService.rotateRefreshToken(refreshToken, response);

        return ResponseEntity.ok().build();

    }

}
