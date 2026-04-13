package com.rishi.authsystem.Controller;

import org.springframework.web.bind.annotation.*;

import com.rishi.authsystem.DTO.UserResponse;
import com.rishi.authsystem.Service.UsersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String home() {
        return "Backend is LIVE 🚀";
    }

    // ✅ Get current logged-in user
    @GetMapping("/profile")
    public UserResponse getUser() {
        log.info("GET /users/profile called");
        return usersService.getCurrentUser();
    }

    // ✅ Get user by email
    @GetMapping("/email")
    public UserResponse getByEmail(@RequestParam String email) {
        log.info("GET /users/email called with email: {}", email);
        return usersService.getByEmail(email);
    }

    // ✅ Check if email exists (better API response)
    @GetMapping("/exists")
    public Object existsByEmail(@RequestParam String email) {
        log.info("GET /users/exists called with email: {}", email);

        boolean exists = usersService.checkByEmail(email);

        return java.util.Map.of(
                "email", email,
                "exists", exists);
    }
}

//// https://13.204.244.181/auth/login
// https://13.204.244.181/
