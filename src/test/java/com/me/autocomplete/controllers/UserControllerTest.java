package com.me.autocomplete.controllers;

import com.me.autocomplete.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    //test for adding user
    @Test
    void addUser() {
        //given - create mock user with data
        User mockUser = new User();
        mockUser.setName("Pelle Svanslös");
        mockUser.setEmail("pelle@svans.se");
        mockUser.setAddress("kattgatan 11");
        //createCustomer method will always return the mocked user
        when(userService.createUser(any(UserDto.class))).thenReturn(mockUser);

        //when - .addUser endpoint is called, it will call on .createCustomer internally, which is mocked with above data
        ResponseEntity<String> responseEntity = userController.addUser(new UserDto());

        //then - assertions
        assertEquals("customer added: Pelle Svanslös", responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Pelle Svanslös", mockUser.getName());
        assertEquals("pelle@svans.se", mockUser.getEmail());
        assertEquals("kattgatan 11", mockUser.getAddress());
    }
}