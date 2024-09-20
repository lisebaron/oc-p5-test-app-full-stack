package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SessionMapperImplTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private Session session;
    private SessionDto sessionDto;
    private Teacher teacher;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Test Session");
        sessionDto.setDescription("Description");
        sessionDto.setTeacher_id(1L);
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        session = new Session();
        session.setId(1L);
        session.setName("Test Session");
        session.setDescription("Description");

        teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        user = new User();
        user.setId(1L);
        session.setUsers(List.of(user));
    }

    @Test
    void toEntity_shouldMapSessionDtoToEntity() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getTeacher_id(), result.getTeacher().getId());
        assertEquals(2, result.getUsers().size());
        verify(teacherService).findById(1L);
        verify(userService).findById(1L);
    }

    @Test
    void toDto_shouldMapSessionToDto() {
        SessionDto result = sessionMapper.toDto(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getDate(), result.getDate());
        assertEquals(session.getTeacher().getId(), result.getTeacher_id());
        assertEquals(1, result.getUsers().size());
    }

    @Test
    void toEntityList_shouldMapDtoListToEntityList() {
        List<SessionDto> dtoList = List.of(sessionDto);
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user);

        List<Session> result = sessionMapper.toEntity(dtoList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sessionDto.getId(), result.get(0).getId());
    }

    @Test
    void toDtoList_shouldMapEntityListToDtoList() {
        List<Session> entityList = List.of(session);

        List<SessionDto> result = sessionMapper.toDto(entityList);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(session.getId(), result.get(0).getId());
    }

    @Test
    void toEntityList_shouldReturnNullWhenDtoListIsNull() {
        assertNull(sessionMapper.toEntity((List<SessionDto>) null));
    }

    @Test
    void toDtoList_shouldReturnNullWhenEntityListIsNull() {
        assertNull(sessionMapper.toDto((List<Session>) null));
    }
}

