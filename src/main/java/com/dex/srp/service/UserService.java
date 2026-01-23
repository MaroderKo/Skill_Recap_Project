package com.dex.srp.service;

import com.dex.srp.domain.User;
import com.dex.srp.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {
    public List<User> findAll() {
        log.info(">findAll function of UserService");
        return List.of(new User(1, "test@example.com"));
    }

    public User findById(long id) {
        throw new UserNotFoundException(String.format("User with id %d not found", id));
    }
}
