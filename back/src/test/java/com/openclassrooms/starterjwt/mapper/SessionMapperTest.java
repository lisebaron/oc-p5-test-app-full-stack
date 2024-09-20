package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Session");
        sessionDto.setDescription("Description of Session 1");
        sessionDto.setTeacher_id(7L);
        sessionDto.setUsers(List.of(6L));

        Teacher teacher = new Teacher();
        teacher.setId(7L);
        when(teacherService.findById(7L)).thenReturn(teacher);

        User user = new User();
        user.setId(6L);
        when(userService.findById(6L)).thenReturn(user);

        Session session = sessionMapper.toEntity(sessionDto);

        verify(teacherService).findById(7L);
        verify(userService).findById(6L);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(user.getId(), session.getUsers().get(0).getId());
    }

    @Test
    void toMapping_shouldMapEntityToDto() {
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

        when(userService.findById(6L)).thenReturn(user);

        SessionDto sessionDto = sessionMapper.toDto(session);

        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().get(0).getId(), user.getId());
    }

}
