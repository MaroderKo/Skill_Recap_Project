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


    /**
     * Saves user.
     *
     * @param userDto user data
     * @return created user
     */
    public User save(UserDto userDto) {
        return save(new User(null, userDto.email(), userDto.username(), userDto.age()));
    }

    public User save(User user) {
        log.info("save user with email : {}", user.getEmail());
        return userRepository.save(user);
    }

    /**
     * Returns all users.
     *
     * @return list of all users
     */
    public List<User> findAll() {
        log.info(">findAll function of UserService");
        return userRepository.findAll();
    }

    /**
     * Returns user by given identifier.
     *
     * @param id user identifier.
     * @return existing user
     * @throws UserNotFoundException when user with given id does not exist
     */
    public User findById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Updates mutable user fields.
     *
     * @param id user identifier
     * @param dto data to update
     * @return updated user
     * @throws UserNotFoundException when user with given id does not exist
     */
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

    /**
     * Deletes user by identifier.
     *
     * @param id user identifier
     */
    public void delete(long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

}
