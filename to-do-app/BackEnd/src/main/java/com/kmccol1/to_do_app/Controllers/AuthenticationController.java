package com.kmccol1.to_do_app.Controllers;

import com.kmccol1.to_do_app.payload.JwtResponse;
import com.kmccol1.to_do_app.payload.RegisterRequest;
import com.kmccol1.to_do_app.security.UserDetailsImpl;
import com.kmccol1.to_do_app.security.jwt.JwtUtils;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.kmccol1.to_do_app.payload.LoginRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User registeredUser = userService.registerUser(registerRequest);
            return ResponseEntity.ok("User registered successfully: " + registeredUser.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(String.valueOf(authentication));

        UserDetailsImpl userDetails = UserDetailsImpl.build((User) authentication.getPrincipal());

        // Create and return the JwtResponse object
        //JwtResponse jwtResponse = new JwtResponse(jwt, String.valueOf(userDetails.getId()), userDetails.getUsername(), userDetails.getEmail());
        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail());

        return ResponseEntity.ok(jwtResponse);
    }
}