package com.bank.controller;

import com.bank.dto.LoginResponse;
import com.bank.dto.RegisterRequest;
import com.bank.dto.RegisterResponse;
import com.bank.service.AuthService;
import com.bank.service.CustomUserDetailsService;
import com.bank.util.JwtUtil;
import com.bank.util.ResponseHandler;
import com.bank.validation.FullUpdate;
import com.bank.validation.PartialUpdate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Validated(FullUpdate.class) RegisterRequest registerRequest) {
        if (authService.userByEmailExists(registerRequest.getEmail())) {
            log.info("User with email: {} can't be registered. It already exists.", registerRequest.getEmail());

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "User already exists",
                    null
            );
        }

        RegisterResponse registerResponse = authService.register(registerRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getUsername(), registerRequest.getPassword())
        );
        String jwtToken = jwtUtil.generateToken(registerResponse.getUsername());

        registerResponse.setToken(jwtToken);

        log.info("User with email {} registered successfully", registerResponse.getUsername());

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                false,
                "User registered successfully",
                registerResponse
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Validated(PartialUpdate.class) RegisterRequest registerRequest) {
        if (registerRequest.getEmail() == null && registerRequest.getUsername() == null) {
            log.info("Email or username is required while logging in");

            return ResponseHandler.generateResponse(
                    HttpStatus.BAD_REQUEST,
                    true,
                    "Email or username is required",
                    null
            );
        }

        UserDetails userDetails = registerRequest.getEmail() != null ?
                userDetailsService.loadUserByEmail(registerRequest.getEmail()) :
                userDetailsService.loadUserByUsername(registerRequest.getUsername());

        if (!authService.isValidPassword(registerRequest.getPassword(), userDetails.getPassword())) {
            log.info("Invalid password for user with email: {}", userDetails.getUsername());

            return ResponseHandler.generateResponse(
                    HttpStatus.UNAUTHORIZED,
                    true,
                    "Invalid password",
                    null
            );
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(), registerRequest.getPassword())
        );
        String jwtToken = jwtUtil.generateToken(userDetails.getUsername());

        log.info("User with email {} logged in successfully", userDetails.getUsername());

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                false,
                "User logged in successfully",
                new LoginResponse(jwtToken)
        );
    }
}
