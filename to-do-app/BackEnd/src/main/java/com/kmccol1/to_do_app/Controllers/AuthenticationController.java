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

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
    {
        try
        {
            User registeredUser = userService.registerUser(registerRequest);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", registeredUser.getUsername());
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace(); // For debugging purposes
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Cast to UserDetailsImpl instead of User
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        // Create and return the JwtResponse object
        JwtResponse jwtResponse = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail());

        return ResponseEntity.ok(jwtResponse);
    }
}