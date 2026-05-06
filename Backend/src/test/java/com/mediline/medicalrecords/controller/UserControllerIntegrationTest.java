package com.mediline.medicalrecords.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediline.medicalrecords.entity.User;
import com.mediline.medicalrecords.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // Task 5.2: Integration tests for UserController

    @Test
    void createUser_WithValidData_ShouldReturn201() throws Exception {
        // Given
        String userJson = """
            {
                "name": "John Doe",
                "email": "john@example.com",
                "phone": "1234567890",
                "birthDate": "1990-01-01"
            }
            """;

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void createUser_WithInvalidEmail_ShouldReturn400() throws Exception {
        // Given
        String userJson = """
            {
                "name": "John Doe",
                "email": "invalid-email",
                "phone": "1234567890"
            }
            """;

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldReturn400() throws Exception {
        // Given
        User existingUser = new User();
        existingUser.setName("Existing User");
        existingUser.setEmail("duplicate@example.com");
        userRepository.save(existingUser);

        String userJson = """
            {
                "name": "New User",
                "email": "duplicate@example.com"
            }
            """;

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setName("User 1");
        user1.setEmail("user1@example.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User 2");
        user2.setEmail("user2@example.com");
        userRepository.save(user2);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("User 1", "User 2")));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        // Given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");
        User saved = userRepository.save(user);

        // When & Then
        mockMvc.perform(get("/api/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldReturn404() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        // Given
        User user = new User();
        user.setName("Original Name");
        user.setEmail("original@example.com");
        User saved = userRepository.save(user);

        String updateJson = """
            {
                "name": "Updated Name",
                "email": "updated@example.com"
            }
            """;

        // When & Then
        mockMvc.perform(put("/api/users/{id}", saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void updateUser_WhenUserNotExists_ShouldReturn404() throws Exception {
        // Given
        String updateJson = """
            {
                "name": "Updated Name"
            }
            """;

        // When & Then
        mockMvc.perform(put("/api/users/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturn204() throws Exception {
        // Given
        User user = new User();
        user.setName("To Delete");
        user.setEmail("delete@example.com");
        User saved = userRepository.save(user);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // Verify user is deleted
        mockMvc.perform(get("/api/users/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldReturn404() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}
