package com.cems.service;

import com.cems.model.User;
import com.cems.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        Optional<User> existingUser = repository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT"); // Default role
        }

        return repository.save(user);
    }

    public User authenticateUser(String email, String password) {
        Optional<User> user = repository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public User getUserById(String id) {
        return repository.findById(id).orElse(null);
    }

    public User updateUserProfile(String id, User updatedDetails) {
        return repository.findById(id).map(user -> {
            user.setName(updatedDetails.getName());
            user.setPhoneNumber(updatedDetails.getPhoneNumber());
            user.setDepartment(updatedDetails.getDepartment());
            user.setCourse(updatedDetails.getCourse());
            user.setRollNumber(updatedDetails.getRollNumber());
            user.setUniversityYear(updatedDetails.getUniversityYear());
            user.setDateOfBirth(updatedDetails.getDateOfBirth());
            user.setGender(updatedDetails.getGender());
            user.setAddress(updatedDetails.getAddress());
            user.setCollege(updatedDetails.getCollege());
            return repository.save(user);
        }).orElse(null);
    }

    public boolean resetPassword(String email, String newPassword) {
        Optional<User> userOpt = repository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
            return true;
        }
        return false;
    }
}
