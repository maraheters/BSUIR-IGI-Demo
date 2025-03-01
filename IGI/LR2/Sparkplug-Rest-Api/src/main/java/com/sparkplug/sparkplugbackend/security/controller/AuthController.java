package com.sparkplug.sparkplugbackend.security.controller;

import com.sparkplug.sparkplugbackend.security.dto.AuthRequestDto;
import com.sparkplug.sparkplugbackend.security.dto.AuthResponseDto;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import com.sparkplug.sparkplugbackend.security.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthRequestDto authRequest) {
        return ResponseEntity.ok(
                authService.register(authRequest.username(), authRequest.password()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequest) {
        return ResponseEntity.ok(
                authService.verify(authRequest.username(), authRequest.password()));
    }

    @GetMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authenticate(@AuthenticationPrincipal SparkplugUserDetails userDetails){
        return ResponseEntity.ok(
                authService.verify(userDetails));
    }
}
