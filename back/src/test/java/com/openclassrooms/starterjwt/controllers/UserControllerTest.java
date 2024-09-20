package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    private JsonMapper mapper = new JsonMapper();

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;

    private UserController controller;

    @BeforeEach
    public void setUp() {
        controller = new UserController(userService, userMapper);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findById_shouldFindUserById() throws Exception {
        final var id = 3L;
        User user = new User();
        user.setId(id);
        user.setEmail("test@example.com");

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setEmail("test@example.com");

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        final var result = mockMvc.perform(get("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
        result.andExpect(jsonPath("$.id").value(id));
        result.andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        final var id = 1L;
        when(userService.findById(id)).thenReturn(null);

        final var result = mockMvc.perform(get("/api/user/" + id));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        final var result = mockMvc.perform(get("/api/user/InvalidId"));

        result.andExpect(status().isBadRequest());
    }

    @Test
    void save_shouldDeleteUser() throws Exception {
        final var id = 3L;
        User user = new User();
        user.setId(id);
        user.setEmail("test@example.com");

        when(userService.findById(id)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        // Mock SecurityContextHolder to return userDetails when getPrincipal() is called
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        final var result = mockMvc.perform(delete("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        verify(userService).delete(id);
    }

    @Test
    void save_shouldReturnUnauthorized() throws Exception {
        final var id = 3L;
        User user = new User();
        user.setId(id);
        user.setEmail("test@example.com");

        when(userService.findById(id)).thenReturn(user);
        when(userDetails.getUsername()).thenReturn("different.email@example.com");

        // Mock SecurityContextHolder to return userDetails when getPrincipal() is called
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        final var result = mockMvc.perform(delete("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isUnauthorized());
    }

    @Test
    void save_shouldReturnNotFound() throws Exception {
        final var id = 3L;
        User user = new User();
        user.setId(id);
        user.setEmail("test@example.com");
        when(userService.findById(id)).thenReturn(null);

        final var result = mockMvc.perform(delete("/api/user/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isNotFound());
    }

    @Test
    void save_shouldReturnBadRequest() throws Exception {
        final var result = mockMvc.perform(delete("/api/user/InvalidId")
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isBadRequest());
    }

}