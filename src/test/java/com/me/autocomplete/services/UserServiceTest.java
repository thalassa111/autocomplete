package com.me.autocomplete.services;

import com.me.autocomplete.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    public UserRepository userRepository;

    @Mock
    public PasswordEncoderUtil passwordEncoder;

    @InjectMocks
    public UserService userService;

    @Test
    public void testCreateCustomer() throws Exception{
        //dto to be sent into the test method
        UserDto userDto = new UserDto("Pelle", "pelle@svanslos.com", "kattgatan 1", "password");

        //mocking of passwordEncoder.generateSalt()
        when(passwordEncoder.generateSalt()).thenReturn("$2a$10$someSalt");
        String salt = passwordEncoder.generateSalt();
        //mocking of passwordEncoder.encodePassword
        when(passwordEncoder.encodePassword(userDto.getPassword(), salt)).thenReturn("hashedPassword");
        //mocking of userRepository.save
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            return savedUser;
        });

        //method to be tested
        User user = userService.createUser(userDto);

        //assertions
        assertNotNull(user);
        assertEquals("Pelle", user.getName());
        assertEquals("pelle@svanslos.com", user.getEmail());
        assertEquals("kattgatan 1", user.getAddress());
        assertEquals("hashedPassword", user.getPassword());
        assertEquals("$2a$10$someSalt", user.getSalt());

        verify(userRepository).save(user);
    }
}
