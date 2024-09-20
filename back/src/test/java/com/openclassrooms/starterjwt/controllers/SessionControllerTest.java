package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        Session session = new Session();
        SessionDto sessionDto = new SessionDto();

        when(sessionService.getById(anyLong())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        final var result = mockMvc.perform(get("/api/session/1"));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        when(sessionService.getById(anyLong())).thenReturn(null);

        final var result = mockMvc.perform(get("/api/session/1"));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        final var result = mockMvc.perform(get("/api/session/InvalidId"));

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
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(2L);
        sessionDto.setName("Session 2");
        sessionDto.setDescription("Description of Session 2");
        sessionDto.setDate(new Date(2024, 7, 7));
        sessionDto.setTeacher_id(2L);
        sessionDto.setUsers(List.of(2L));

        Teacher teacher = new Teacher();
        teacher.setId(9L);

        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setId(2L);
        session.setName("Session 2");
        session.setDate(new Date(2024,7,7));
        session.setDescription("Description of Session 2");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        final var result = mockMvc.perform(post("/api/session")
                .content(mapper.writeValueAsString(sessionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }

    @Test
    void update_shouldUpdateSession() throws Exception {
        final var id = 1L;

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName("Session 1");
        sessionDto.setDescription("Description of Session 1");
        sessionDto.setDate(new Date(2024, 7, 7));
        sessionDto.setTeacher_id(2L);
        sessionDto.setUsers(List.of(2L));

        Teacher teacher = new Teacher();
        teacher.setId(9L);

        User user = new User();
        user.setId(2L);

        Session session = new Session();
        session.setId(id);
        session.setName("Session 1");
        session.setDate(new Date(2024,7,7));
        session.setDescription("Description of Session 1");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        final var result = mockMvc.perform(put("/api/session/" + id)
                .content(mapper.writeValueAsString(sessionDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }

    @Test
    void save_shouldDeleteSession() throws Exception {
        final var id = 1L;
        Session session = new Session();
        when(sessionService.getById(id)).thenReturn(session);

        final var result = mockMvc.perform(delete("/api/session/" + id));

        result.andExpect(status().isOk());
    }

    @Test
    void save_shouldReturnNotFound() throws Exception {
        final var id = 1L;
        Session session = new Session();
        when(sessionService.getById(id)).thenReturn(null);

        final var result = mockMvc.perform(delete("/api/session/" + id));

        result.andExpect(status().isNotFound());
    }

    @Test
    void save_shouldReturnBadRequest() throws Exception {
        final var result = mockMvc.perform(delete("/api/session/invalidId"));
        result.andExpect(status().isBadRequest());
    }

    @Test
    void participate_shouldAddParticipant() throws Exception {
        final var result = mockMvc.perform(post("/api/session/2/participate/1"));
        result.andExpect(status().isOk());
    }

    @Test
    void participate_shouldReturnBadRequest_whenUserIdIsInvalid() throws Exception {
        final var result = mockMvc.perform(post("/api/session/2/participate/invalidId"));
        result.andExpect(status().isBadRequest());
    }

    @Test
    void participate_shouldReturnBadRequest_whenIdIsInvalid() throws Exception {
        final var result = mockMvc.perform(post("/api/session/invalidId/participate/1"));
        result.andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_shouldRemoveParticipant() throws Exception {
        final var result = mockMvc.perform(delete("/api/session/2/participate/1"));
        result.andExpect(status().isOk());
    }

    @Test
    void noLongerParticipate_shouldRemoveParticipant_whenUserIdIsInvalid() throws Exception {
        final var result = mockMvc.perform(delete("/api/session/2/participate/invalidId"));
        result.andExpect(status().isBadRequest());
    }

    @Test
    void noLongerParticipate_shouldRemoveParticipant_whenIdIsInvalid() throws Exception {
        final var result = mockMvc.perform(delete("/api/session/invalidId/participate/1"));
        result.andExpect(status().isBadRequest());
    }
}