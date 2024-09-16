package com.leski.service;

import com.leski.model.User;
import com.leski.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        repository.existsByUsername(user.getUsername());
        return save(user);
    }
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> null);
    }

}
