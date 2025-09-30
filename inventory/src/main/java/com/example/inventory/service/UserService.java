package com.example.inventory.service;

import com.example.inventory.dto.SignupRequest;
import com.example.inventory.model.ERole;
import com.example.inventory.model.Role;
import com.example.inventory.model.User;
import com.example.inventory.repository.RoleRepository;
import com.example.inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(SignupRequest signUpRequest) {
        try {
            // Ensure roles are initialized
            initializeRoles();
            
            // Create new user
            User user = new User(signUpRequest.getUsername(),
                               signUpRequest.getEmail(),
                               encoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = new HashSet<>();

            // Set default role to USER
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role ROLE_USER is not found in database."));
            roles.add(userRole);

            user.setRoles(roles);
            return userRepository.save(user);
        } catch (Exception e) {
            System.err.println("Error during user registration: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to register user: " + e.getMessage(), e);
        }
    }

    public void initializeRoles() {
        if (!roleRepository.findByName(ERole.ROLE_USER).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_USER));
        }
        if (!roleRepository.findByName(ERole.ROLE_ADMIN).isPresent()) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
    }
}