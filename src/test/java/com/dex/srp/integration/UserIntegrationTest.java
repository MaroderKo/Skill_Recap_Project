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
class UserIntegrationTest {

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
        UserDto userDto = new UserDto("test@example.com", "user1", 18);
        ResponseEntity<User> response = restTemplate.postForEntity("/users", userDto, User.class);
        User responseUser = response.getBody();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(responseUser).isNotNull();
        assertThat(responseUser.getEmail()).isEqualTo("test@example.com");
        assertThat(responseUser.getUsername()).isEqualTo("user1");
        assertThat(responseUser.getAge()).isEqualTo(18);

    }

    @Test
    void updateUser() {
        saveUser(new UserDto("test@example.com", "user1", 18));

        UserDto userDto = new UserDto(null, "user1", 18);
        UserDto responseUser = restTemplate.patchForObject("/users/1", userDto, UserDto.class);

        assertThat(responseUser).isNotNull();
        assertThat(responseUser.email()).isEqualTo("test@example.com");
        assertThat(responseUser.username()).isEqualTo("user1");
        assertThat(responseUser.age()).isEqualTo(18);
    }

    @Test
    void readAllUsers() {
        User user = saveUser(new UserDto("test@example.com", "user1", 18));
        User user1 = saveUser(new UserDto("test1@example.com", "user2", 19));

        ResponseEntity<String> response = restTemplate.getForEntity("/users", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        String responseBody = response.getBody();

        assertThat((String) JsonPath.read(responseBody, "$[0].email")).isEqualTo(user.getEmail());
        assertThat((String) JsonPath.read(responseBody, "$[0].username")).isEqualTo(user.getUsername());
        assertThat((Integer) JsonPath.read(responseBody, "$[0].age")).isEqualTo(user.getAge());
        assertThat((String) JsonPath.read(responseBody, "$[1].email")).isEqualTo(user1.getEmail());
        assertThat((String) JsonPath.read(responseBody, "$[1].username")).isEqualTo(user1.getUsername());
        assertThat((Integer) JsonPath.read(responseBody, "$[1].age")).isEqualTo(user1.getAge());
    }

    @Test
    void readUserPositive() {
        User user = saveUser(new UserDto("test@example.com", "user1", 18));

        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        String responseBody = response.getBody();
        assertThat((String) JsonPath.read(responseBody, "$.email")).isEqualTo(user.getEmail());
        assertThat((Integer) JsonPath.read(responseBody, "$.age")).isEqualTo(user.getAge());
    }

    @Test
    void readUserNegative() {
        ResponseEntity<String> response = restTemplate.getForEntity("/users/1", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        String responseBody = response.getBody();
        assertThat((String) JsonPath.read(responseBody, "$.error_code")).isEqualTo("ENTITY_NOT_FOUND");
        assertThat((String) JsonPath.read(responseBody, "$.path")).isEqualTo("/users/1");
        assertThat((String) JsonPath.read(responseBody, "$.trace_id")).isNotNull();
        assertThat((String) JsonPath.read(responseBody, "$.timestamp")).isNotNull();
    }

    @Test
    void deleteUser() {
        saveUser(new UserDto("test@example.com", "user1", 18));
        ResponseEntity<Void> response = restTemplate.exchange("/users/1",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        assertThat(restTemplate.getForEntity("/users/1", String.class).getStatusCode().value()).isEqualTo(404);

    }


}
