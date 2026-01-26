package com.dex.srp.integration;

import com.dex.srp.domain.User;
import com.dex.srp.domain.dto.UserDto;
import com.dex.srp.exception.UserNotFoundException;
import com.dex.srp.service.UserService;
import com.jayway.jsonpath.JsonPath;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestRestTemplate
class UserIntegrationalTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.7-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;


    @BeforeEach
    void setUp(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void createUser() {
        UserDto userDto = new UserDto("test@example.com");
        ResponseEntity<User> response = restTemplate.postForEntity("/users", userDto, User.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getEmail()).isEqualTo("test@example.com");

    }

    @Test
    void updateUser() {
        User user = new User(null, "test@example.com");
        userService.save(user);
        UserDto userDto = new UserDto("test1@example.com");
        User responseUser = restTemplate.patchForObject("/users/1", userDto, User.class);

        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getId()).isEqualTo(1L);
        assertThat(responseUser.getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    void readAllUsers() {
        User user = new User(null, "test@example.com");
        userService.save(user);
        User user1 = new User(null, "test1@example.com");
        userService.save(user1);

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        String responseBody = response.getBody();
        assertThat((Integer) JsonPath.read(responseBody, "$[0].id")).isEqualTo(1);
        assertThat((String) JsonPath.read(responseBody, "$[0].email")).isEqualTo(user.getEmail());
        assertThat((Integer) JsonPath.read(responseBody, "$[1].id")).isEqualTo(2);
        assertThat((String) JsonPath.read(responseBody, "$[1].email")).isEqualTo(user1.getEmail());
    }

    @Test
    void readUserPostitve() {
        User user = new User(null, "test@example.com");
        userService.save(user);

        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        String responseBody = response.getBody();
        assertThat((Integer) JsonPath.read(responseBody, "$.id")).isEqualTo(1);
        assertThat((String) JsonPath.read(responseBody, "$.email")).isEqualTo(user.getEmail());
    }

    @Test
    void readUserNegative() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        String responseBody = response.getBody();
        assertThat((String) JsonPath.read(responseBody, "$.code")).isEqualTo("ENTITY_NOT_FOUND");
        assertThat((String) JsonPath.read(responseBody, "$.path")).isEqualTo("/users/1");
    }

    @Test
    void deleteUser() {
        User user = new User(null, "test@example.com");
        userService.save(user);
        ResponseEntity<Void> response = restTemplate.exchange("/users/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        assertThrows(UserNotFoundException.class, () -> userService.findById(1));

    }


}
