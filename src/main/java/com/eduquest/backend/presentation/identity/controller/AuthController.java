package com.eduquest.backend.presentation.identity.controller;

import com.eduquest.backend.application.identity.service.AuthService;
import com.eduquest.backend.presentation.identity.dto.request.FindIdRequest;
import com.eduquest.backend.presentation.identity.dto.request.FindPasswordRequest;
import com.eduquest.backend.presentation.identity.dto.request.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("Sign In");
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@Valid @RequestBody FindIdRequest request) {

        authService.sendFindIdEmail(request.email());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@Valid @RequestBody FindPasswordRequest request) {

        authService.sendPasswordRestEmail(request.email(), request.userId());

        return ResponseEntity.status(204).build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request.token(), request.newPassword());

        return ResponseEntity.status(204).build();
    }

    @GetMapping("/sign-up/{token}")
    public void verifySignUp(@PathVariable("token") String token, HttpServletResponse response) throws IOException {

        authService.verifySignUpToken(token);

        response.sendRedirect(frontendUrl);

    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        authService.rotateRefreshToken(refreshToken, response);

        return ResponseEntity.ok().build();

    }

}
