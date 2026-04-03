package com.eduquest.backend.presentation.identity.controller;

import com.eduquest.backend.presentation.identity.dto.request.FindIdRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.ok("Sign In");
    }

    @PostMapping("/find-id")
    public ResponseEntity<String> findId(@Valid FindIdRequest request) {
        return ResponseEntity.status(204).build();
    }

}
