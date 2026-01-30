package com.dex.srp.service;

import com.dex.srp.domain.User;
import com.dex.srp.domain.dto.UserDto;
import com.dex.srp.exception.UserNotFoundException;
import com.dex.srp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(UserDto userDto) {
        return save(new User(null, userDto.email(), userDto.username(), userDto.age()));
    }

    public User save(User user) {
        log.info("save user with email : {}", user.getEmail());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        log.info(">findAll function of UserService");
        return userRepository.findAll();
    }

    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User update(long id, UserDto dto) {
        User user = findById(id);
        if (dto.email() != null) {
            user.setEmail(dto.email());
        }
        if (dto.username() != null) {
            user.setUsername(dto.username());
        }
        if (dto.age() != null) {
            user.setAge(dto.age());
        }
        return save(user);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

}
