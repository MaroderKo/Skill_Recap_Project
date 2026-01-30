package com.dex.srp.integration;

import com.dex.srp.domain.User;
import com.dex.srp.domain.dto.UserDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    JdbcTemplate jdbcTemplate;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("truncate table users RESTART IDENTITY CASCADE ");
    }

    private User saveUser(UserDto userDto) {
        return restTemplate.postForEntity("/users", userDto, User.class).getBody();
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
        saveUser(new UserDto("test@example.com"));

        UserDto userDto = new UserDto("test1@example.com");
        User responseUser = restTemplate.patchForObject("/users/1", userDto, User.class);

        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getId()).isEqualTo(1L);
        assertThat(responseUser.getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    void readAllUsers() {
        User user = saveUser(new UserDto("test@example.com"));
        User user1 = saveUser(new UserDto("test1@example.com"));

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        String responseBody = response.getBody();

        assertThat((Integer) JsonPath.read(responseBody, "$[0].id")).isEqualTo(1);
        assertThat((String) JsonPath.read(responseBody, "$[0].email")).isEqualTo(user.getEmail());
        assertThat((Integer) JsonPath.read(responseBody, "$[1].id")).isEqualTo(2);
        assertThat((String) JsonPath.read(responseBody, "$[1].email")).isEqualTo(user1.getEmail());
    }

    @Test
    void readUserPositive() {
        User user = saveUser(new UserDto("test@example.com"));

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
        assertThat((String) JsonPath.read(responseBody, "$.error_code")).isEqualTo("ENTITY_NOT_FOUND");
        assertThat((String) JsonPath.read(responseBody, "$.path")).isEqualTo("/users/1");
    }

    @Test
    void deleteUser() {
        saveUser(new UserDto("test@example.com"));
        ResponseEntity<Void> response = restTemplate.exchange("/users/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        assertThat(restTemplate.getForEntity("/users/1", String.class).getStatusCode().value()).isEqualTo(404);

    }


}
