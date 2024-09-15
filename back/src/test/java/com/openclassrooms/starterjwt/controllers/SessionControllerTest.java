package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    private MockMvc mockMvc;

    private JsonMapper mapper = new JsonMapper();

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private SessionController controller;

    @BeforeEach
    public void setUp() {
        controller = new SessionController(sessionService, sessionMapper);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findById_shouldReturnSession_Success() throws Exception {
        //Arrange
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();

        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        //Act
        final var result = mockMvc.perform(get("/api/session/1"));

        //Assert
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        //Arrange
        when(sessionService.getById(anyLong())).thenReturn(null);
        //Act
        final var result = mockMvc.perform(get("/api/session/1"));
        //Assert
        result.andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        //Act
        final var result = mockMvc.perform(get("/api/session/InvalidId"));
        //Assert
        result.andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldReturnSessionList() throws Exception {
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();
        List<Session> sessionList = List.of(session);
        List<SessionDto> sessionDtoList = List.of(sessionDto);

        when(sessionService.findAll()).thenReturn(sessionList);
        when(sessionMapper.toDto(sessionList)).thenReturn(sessionDtoList);

        final var result = mockMvc.perform(get("/api/session"));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void create_shouldCreateSession_success() throws Exception {
        //Arrange
        SessionDto sessionDto = new SessionDto();
        Session session = new Session();

        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        //Act
        final var result = mockMvc.perform(post("/api/session")
                .content(mapper.writeValueAsString(sessionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        //Assert
        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }
}