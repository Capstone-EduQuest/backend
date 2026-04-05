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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("Sign In");
    }

    @PostMapping("/find-userId")
    public ResponseEntity<String> findId(@Valid FindIdRequest request) {

        authService.sendFindIdEmail(request.email());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/find-password")
    public ResponseEntity<String> findPassword(@Valid FindPasswordRequest request) {

        authService.sendPasswordRestEmail(request.email(), request.userId());

        return ResponseEntity.status(204).build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid ResetPasswordRequest request) {

        authService.resetPassword(request.token(), request.newPassword());

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        return ResponseEntity.ok("Refresh Token");
    }

}
