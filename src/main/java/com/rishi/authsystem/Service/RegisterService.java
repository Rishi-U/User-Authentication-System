package com.rishi.authsystem.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.rishi.authsystem.DTO.RegisterRequest;
import com.rishi.authsystem.Model.UserEntity;
import com.rishi.authsystem.Respository.UsersRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegisterService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(RegisterService.class);

    public RegisterService(UsersRepository usersRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterRequest request) {

        log.info("Register attempt for email: {}", request.getEmail());

        // Check duplicate email
        if (usersRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists");
        }

        UserEntity user = new UserEntity();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        usersRepository.save(user);

        log.info("User registered successfully: {}", request.getEmail());

        return "User registered successfully";
    }
}