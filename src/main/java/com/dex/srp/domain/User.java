package com.dex.srp.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
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

    public void changeUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("username is mandatory");
        } else if (username.length() < 5 || username.length() > 255) {
            throw new IllegalArgumentException("username length should be between 3 and 255 characters");
        }
        this.username = username;
    }

    public void changeAge(Integer age) {
        if (age < 0 || age > 100) {
            throw new IllegalArgumentException("age should be between 0 and 100");
        }
        this.age = age;
    }
}
