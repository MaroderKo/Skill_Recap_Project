package com.dex.srp.domain;


import com.dex.srp.exception.DomainValidationException;
import jakarta.persistence.*;
import lombok.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@EqualsAndHashCode
@ToString
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, length = 320)
    private String email;

    @Column(unique = true)
    private String username;

    @Column
    private Integer age;

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

    @Builder
    public User(String email, String username, Integer age) {
        changeEmail(email);
        if (username != null) {
            changeUsername(username);
        }
        if (age != null) {
            changeAge(age);
        }
    }

    public void changeUsername(String username) {
        if (username == null) {
            throw new DomainValidationException("username", "username is mandatory");
        } else if (username.length() < 5 || username.length() > 255) {
            throw new DomainValidationException("username", "username length should be between 3 and 255 characters");
        }
        this.username = username;
    }

    public void changeEmail(String email) {
        if (email == null) {
            throw new DomainValidationException("email", "email is mandatory");
        }
        if (this.email != null) {
            throw new DomainValidationException("email", "email cannot be changed if already exist");
        }
        if (email.length() < 8 || email.length() > 320) {
            throw new DomainValidationException("email", "Email length must be >= 8 and <= 320 characters long");
        }
        Matcher emailMatcher = EMAIL_PATTERN.matcher(email);
        if (!emailMatcher.matches()) {
            throw new DomainValidationException("email", "Email must be in proper email format");
        } else {
            this.email = email;
        }
    }

    public void changeAge(Integer age) {
        if (age == null) {
            return;
        }
        if (age < 0 || age > 100) {
            throw new DomainValidationException("age", "age should be between 0 and 100");
        }
        this.age = age;
    }
}
