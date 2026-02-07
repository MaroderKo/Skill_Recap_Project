package com.dex.srp.controller;

import com.dex.srp.domain.User;
import com.dex.srp.domain.dto.UserDto;
import com.dex.srp.exception.UserNotFoundException;
import com.dex.srp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User createUser(String email, String username, int age) {
        return User.builder()
                .email(email)
                .username(username)
                .age(age)
                .build();
    }

    @Test
    void testFindAllUsers() throws Exception {
        var users = List.of(createUser("test@example.com", "user1", 18));
        Mockito.when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].age").value(18));
    }

    @Test
    void testPositiveFindUserById() throws Exception {
        User user = createUser("test@example.com", "user1", 18);
        Mockito.when(userService.findById(1)).thenReturn(user);
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void testNegativeFindUserById() throws Exception {
        Mockito.when(userService.findById(1)).thenThrow(new UserNotFoundException(1L));
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Entity not found"))
                .andExpect(jsonPath("$.error_code").value("ENTITY_NOT_FOUND"))
                .andExpect(jsonPath("$.details.user_id").value("User with id 1 not found"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDto userDto = new UserDto(null, "user1", 18);
        Mockito.when(userService.update(1, userDto)).thenReturn(createUser("test@example.com", "user1", 18));

        mockMvc.perform(patch("/users/1").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }


}
