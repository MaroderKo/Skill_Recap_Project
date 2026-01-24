package com.dex.srp.repository.impl.inmemory;

import com.dex.srp.domain.User;
import com.dex.srp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserRepository implements UserRepository {

    private static final Map<Long, User> storage = new HashMap<>();

    @PostConstruct
    static void populateStorage() {
        storage.put(1L, new User(1, "test@example.com"));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
