package com.dex.srp.controller;

import com.dex.srp.domain.ValidationGroup;
import com.dex.srp.domain.dto.UserDto;
import com.dex.srp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> save(@Validated(ValidationGroup.OnCreate.class) @RequestBody UserDto userDto) {
        return ResponseEntity.ok(new UserDto(userService.save(userDto)));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable long id) {
        return ResponseEntity.ok(new UserDto(userService.findById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable long id, @Validated(ValidationGroup.OnUpdate.class) @RequestBody UserDto dto) {
        return ResponseEntity.ok(new UserDto(userService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
