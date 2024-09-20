package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class SessionControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper mapper = new ObjectMapper();

    private Teacher teacher;
    private User user;
    private User userTwo;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        teacher = teacherService.findById(1L);
        user = userService.findById(1L);
        userTwo = userService.findById(2L);
    }

    @Test
    void findById_shouldReturnSession() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(get("/api/session/" + savedSession.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Session 7"))
                .andExpect(jsonPath("$.description").value("Description de la session 7"))
                .andExpect(jsonPath("$.teacher_id").value("1"));
    }

    @Test
    void findAll_shouldReturnSessions() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void create_shouldCreateSession() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(7L);
        sessionDto.setName("Session 7");
        sessionDto.setDescription("Description de la session 7");
        sessionDto.setDate(new Date(2024 - 1900, 11, 6));
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        mockMvc.perform(post("/api/session")
                        .content(mapper.writeValueAsBytes(sessionDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Session 7"))
                .andExpect(jsonPath("$.description").value("Description de la session 7"))
                .andExpect(jsonPath("$.teacher_id").value("1"));
    }

    @Test
    void update_shouldUpdateSession() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>(List.of(user)));

        Session savedSession = sessionService.create(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Session updated");
        sessionDto.setDescription("Cette description a été mise a jour");
        sessionDto.setDate(new Date(2024 - 1900, 11, 6));
        sessionDto.setTeacher_id(teacher.getId());
        sessionDto.setUsers(List.of(user.getId()));

        mockMvc.perform(put("/api/session/" + savedSession.getId())
                        .content(mapper.writeValueAsString(sessionDto))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Session updated"))
                .andExpect(jsonPath("$.description").value("Cette description a été mise a jour"))
                .andExpect(jsonPath("$.teacher_id").value("1"));
    }

    @Test
    void save_shouldDeleteSession() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(List.of(user));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + savedSession.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
        assertFalse(sessionRepository.existsById(savedSession.getId()));
    }

    @Test
    public void participate_shouldAddUserToSession() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>(List.of(user)));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(post("/api/session/" + savedSession.getId() + "/participate/2")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void noLongerParticipate_shouldRemoveUserFromSession() throws Exception {
        Session session = new Session();
        session.setName("Session 7");
        session.setDate(new Date(2024 - 1900, 11, 6));
        session.setDescription("Description de la session 7");
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<>(List.of(user, userTwo)));

        Session savedSession = sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + savedSession.getId() + "/participate/" + userTwo.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}
