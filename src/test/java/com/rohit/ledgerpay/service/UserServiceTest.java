package com.rohit.ledgerpay.service;

import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists() {
        String email = "rohit@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser("Rohit", email, "password123"));

        assertEquals("Email already registered: " + email, exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_shouldSaveUser_whenEmailIsNew() {
        String name = "Rohit";
        String email = "rohit@example.com";
        String rawPassword = "password123";
        String hashedPassword = "hashed_password_xyz";

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser(name, email, rawPassword);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(email, result.getEmail());
        assertEquals(hashedPassword, result.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}