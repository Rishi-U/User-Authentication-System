package com.rishi.authsystem.Service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rishi.authsystem.DTO.LoginRequest;
import com.rishi.authsystem.Model.UserEntity;
import com.rishi.authsystem.Respository.UsersRepository;
import com.rishi.authsystem.Security.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    public LoginService(UsersRepository usersRepository,
                        BCryptPasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String loginUser(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        UserEntity dbUser = usersRepository.findByEmail(request.getEmail());

        // 🔥 IMPORTANT: Don't reveal user existence
        if (dbUser == null || 
            !passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {

            log.warn("Login failed for email: {}", request.getEmail());

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password");
        }

        String token = jwtUtil.generateToken(dbUser.getEmail());

        log.info("Login successful for email: {}", request.getEmail());

        return token;
    }
}
