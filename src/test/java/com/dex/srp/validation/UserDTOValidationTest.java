package com.dex.srp.validation;

import com.dex.srp.domain.ValidationGroup;
import com.dex.srp.domain.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ValidationAutoConfiguration.class)
class UserDTOValidationTest {

    @Autowired
    private Validator validator;

    private List<String> getValidationConstraintsErrorList(UserDto userDto, Class<?>... groups) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto, groups);
        return violations.stream().map(ConstraintViolation::getMessage).toList();
    }


    @Test
    void testNullValuesOnCreate() {
        UserDto userDto = new UserDto(null, null, null);

        var violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnCreate.class);
        assertThat(violations).hasSize(2)
                .contains("Email is mandatory")
                .contains("Username must not be blank");
    }

    @Test
    void testNullValuesOnUpdate() {
        UserDto userDto = new UserDto(null, null, null);

        var violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnUpdate.class);
        assertThat(violations).isEmpty();
    }

    @Test
    void testValidationOnCreate() {
        // Test min constraints
        UserDto userDto = new UserDto("a@aa.a", "a", -1);

        var violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnCreate.class);
        assertThat(violations).hasSize(3)
                .contains("Age must be >= 0")
                .contains("Email length must be >= 8 and <= 320 characters long")
                .contains("Username length must be >= 5 and <= 255 characters long");

        //test max constraints

        userDto = new UserDto("a".repeat(321), "a".repeat(256), 101);
        violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnCreate.class);
        assertThat(violations).hasSize(3)
                .contains("Age must be <= 100")
                .contains("Email length must be >= 8 and <= 320 characters long")
                .contains("Username length must be >= 5 and <= 255 characters long");

    }

    @Test
    void testValidationOnUpdate() {
        // Test min constraints
        UserDto userDto = new UserDto(null, "a", -1);

        var violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnUpdate.class);

        assertThat(violations).hasSize(2)
                .contains("Age must be >= 0")
                .contains("Username length must be >= 5 and <= 255 characters long");

        //test max constraints

        userDto = new UserDto(null, "a".repeat(256), 101);
        violations = getValidationConstraintsErrorList(userDto, ValidationGroup.OnUpdate.class);
        assertThat(violations).hasSize(2)
                .contains("Age must be <= 100")
                .contains("Username length must be >= 5 and <= 255 characters long");

    }


}
