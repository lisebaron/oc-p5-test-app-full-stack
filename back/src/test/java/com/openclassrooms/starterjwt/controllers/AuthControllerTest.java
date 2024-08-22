package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private JsonMapper mapper = new JsonMapper();

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private User user;

    private AuthController controller;

    @BeforeEach
    public void setUp(){
        controller = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(eq(authentication))).thenReturn("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9");
    }

    @Test
    void authenticateUser_shouldAuthenticateUser_whenUserFound() throws Exception {
        //Arrange
        final var loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@test.com");
        loginRequest.setPassword("password");

        when(userDetails.getId()).thenReturn(1L);
        when(userDetails.getUsername()).thenReturn("john.doe@test.com");
        when(userDetails.getFirstName()).thenReturn("John");
        when(userDetails.getLastName()).thenReturn("Doe");
        when(userRepository.findByEmail(eq("john.doe@test.com"))).thenReturn(Optional.of(user));
        when(user.isAdmin()).thenReturn(Boolean.FALSE);

        //Act
        final var result = mockMvc.perform(post("/api/auth/login")
                .content(mapper.writeValueAsBytes(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //Assert
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.token").value("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9"));
        result.andExpect(jsonPath("$.id").value("1"));
        result.andExpect(jsonPath("$.firstName").value("John"));
        result.andExpect(jsonPath("$.lastName").value("Doe"));
        result.andExpect(jsonPath("$.username").value("john.doe@test.com"));
        result.andExpect(jsonPath("$.admin").value(Boolean.FALSE));
    }
}