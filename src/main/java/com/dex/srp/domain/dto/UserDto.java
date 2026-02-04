package com.dex.srp.domain.dto;

import com.dex.srp.domain.ValidationGroup.OnCreate;
import com.dex.srp.domain.ValidationGroup.OnUpdate;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record UserDto(
        @Email(message = "Email must be in proper email format", groups = {OnCreate.class}) @Null(message = "Email cannot be changed after registration", groups = {OnUpdate.class}) @Length(min = 8, max = 320, message = "Email length must be >= 8 and <= 320 characters long", groups = {OnCreate.class}) @NotBlank(message = "Email is mandatory", groups = {OnCreate.class}) String email,
        @NotBlank(message = "Username must not be blank", groups = {OnCreate.class}) @Length(min = 5, max = 255, message = "Username length must be >= 5 and <= 255 characters long", groups = {OnCreate.class, OnUpdate.class}) String username,
        @Min(value = 0, message = "Age must be >= 0", groups = {OnCreate.class, OnUpdate.class}) @Max(value = 100, message = "Age must be <= 100", groups = {OnCreate.class, OnUpdate.class}) Integer age) {

}
