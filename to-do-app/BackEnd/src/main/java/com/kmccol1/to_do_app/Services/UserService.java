package com.kmccol1.to_do_app.Services;

import com.kmccol1.to_do_app.Data.RoleRepository;
import com.kmccol1.to_do_app.Data.UserRepository;
import com.kmccol1.to_do_app.Models.Role;
import com.kmccol1.to_do_app.Models.User;
import com.kmccol1.to_do_app.payload.RegisterRequest;
import com.kmccol1.to_do_app.security.UserDetailsImpl;
import jakarta.annotation.PostConstruct;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy // Lazy loading to avoid circular dependency...
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequest registerRequest)
    {
        // Check for existing username or email
        if (userRepository.existsByUsername(registerRequest.getUsername()))
        {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail()))
        {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user
        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(), passwordEncoder.encode(registerRequest.getPassword()));

        // Assign roles - by default, every user gets "ROLE_USER"
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null)
        {
            // If "ROLE_USER" doesn't exist, create it
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    @Transactional
    @PostConstruct
    public void initRoles()
    {
        // Check if roles are already in the database
        if (roleRepository.count() == 0)
        {
            // Creating default roles if not present
            Role roleUser = new Role("ROLE_USER");
            Role roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
        }
    }
}