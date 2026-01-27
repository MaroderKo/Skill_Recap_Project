package com.dex.srp.domain.dto;

import com.dex.srp.domain.User;

public record UserDto(String email){
    public UserDto(User user) {
        this(user.getEmail());
    }
}
