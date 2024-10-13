package com.leski.service;

import com.leski.model.User;
import com.leski.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    public User save(User user) {
        return repository.save(user);
    }
    public boolean existsUser(String username){
        return repository.findById(username).isPresent();
    }
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }
}