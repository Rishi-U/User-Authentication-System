package com.rishi.authsystem.Controller;

import org.springframework.web.bind.annotation.*;

import com.rishi.authsystem.DTO.LoginRequest;
import com.rishi.authsystem.DTO.RegisterRequest;
import com.rishi.authsystem.Service.LoginService;
import com.rishi.authsystem.Service.RegisterService;

import java.util.Map;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterService registerService;
    private final LoginService loginService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(RegisterService registerService,
                          LoginService loginService) {
        this.registerService = registerService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody RegisterRequest request) {

        log.info("POST /auth/register called");

        String message = registerService.registerUser(request);

        return Map.of("Message", message);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest request) {

        log.info("POST /auth/login called");

        String token = loginService.loginUser(request);

        return Map.of("token", token);   // 🔥 return token (not "message")
    }
}