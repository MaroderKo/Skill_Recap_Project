package com.dex.srp.service;

import com.dex.srp.domain.User;
import com.dex.srp.exception.UserNotFoundException;
import com.dex.srp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        log.info(">findAll function of UserService");
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
    }
}
