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

    private User getUserOrThrow(long id)
    {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Saves user.
     *
     * @param userDto user data
     * @return created user
     */
    @Transactional
    public User save(UserDto userDto) {
        User user = User.builder()
            .email(userDto.email())
            .username(userDto.username())
            .age(userDto.age()) .build();
        return userRepository.save(user); }

    /**
     * Returns all users.
     *
     * @return list of all users
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Returns user by given identifier.
     *
     * @param id user identifier.
     * @return existing user
     * @throws UserNotFoundException when user with given id does not exist
     */
    @Transactional(readOnly = true)
    public User findById(long id) {
        return getUserOrThrow(id);
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
        User user = getUserOrThrow(id);
        if (dto.username() != null) {
            user.setUsername(dto.username());
        }
        if (dto.age() != null) {
            user.setAge(dto.age());
        }
        return user;
    }

    /**
     * Deletes user by identifier.
     *
     * @param id user identifier
     */
    @Transactional
    public void delete(long id) {
        User user = getUserOrThrow(id);
        userRepository.delete(user);
    }

}
