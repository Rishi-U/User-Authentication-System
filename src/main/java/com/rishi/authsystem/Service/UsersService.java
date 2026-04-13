package com.rishi.authsystem.Service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rishi.authsystem.DTO.UserResponse;
import com.rishi.authsystem.Model.UserEntity;
import com.rishi.authsystem.Respository.UsersRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    private static final Logger log = LoggerFactory.getLogger(UsersService.class);

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // ✅ Get user by email
    public UserResponse getByEmail(String email) {
        log.info("Fetching user by email: {}", email);

        UserEntity user = usersRepository.findByEmail(email);

        if (user == null) {
            log.warn("User not found: {}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return mapToResponse(user);
    }

    // ✅ Check email exists
    public boolean checkByEmail(String email) {
        log.info("Checking if email exists: {}", email);
        return usersRepository.existsByEmail(email);
    }

    // ✅ Get current logged-in user
    public UserResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            log.warn("Unauthorized access attempt to /profile");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String email = auth.getName();
        log.info("Fetching current user: {}", email);

        UserEntity user = usersRepository.findByEmail(email);

        if (user == null) {
            log.warn("User not found in DB: {}", email);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        return mapToResponse(user);
    }

    // 🔥 COMMON MAPPER (VERY IMPORTANT)
    private UserResponse mapToResponse(UserEntity user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        return res;
    }
}
