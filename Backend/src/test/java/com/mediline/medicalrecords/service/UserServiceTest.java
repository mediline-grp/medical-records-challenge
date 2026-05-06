package com.mediline.medicalrecords.service;

import com.mediline.medicalrecords.entity.User;
import com.mediline.medicalrecords.exception.ResourceNotFoundException;
import com.mediline.medicalrecords.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testUser = new User();
        testUser.setId(testUserId);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setPhone("1234567890");
        testUser.setBirthDate(LocalDate.of(1990, 1, 1));
        testUser.setActive(true);
    }

    // Task 5.1: Unit tests for UserService

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));

        // When
        User found = userService.getUserById(testUserId);

        // Then
        assertNotNull(found);
        assertEquals(testUserId, found.getId());
        assertEquals("John Doe", found.getName());
        verify(userRepository, times(1)).findById(testUserId);
    }

    @Test
    void getUserById_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User created = userService.createUser(testUser);

        // Then
        assertNotNull(created);
        assertEquals(testUserId, created.getId());
        assertEquals("John Doe", created.getName());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        
        User updatedDetails = new User();
        updatedDetails.setName("John Updated");
        updatedDetails.setEmail("john.updated@example.com");
        updatedDetails.setPhone("9876543210");
        updatedDetails.setBirthDate(LocalDate.of(1991, 2, 2));
        updatedDetails.setActive(false);
        
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User updated = userService.updateUser(testUserId, updatedDetails);

        // Then
        assertNotNull(updated);
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(999L, testUser);
        });
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Given
        when(userRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // When
        userService.deleteUser(testUserId);

        // Then
        verify(userRepository, times(1)).findById(testUserId);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
    }
}
