package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// TODO Fix le SessionMapping
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private SessionMapper sessionMapper;

    @BeforeEach
    void setUp() {
        sessionMapper = Mappers.getMapper(SessionMapper.class);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setDescription("Hello");
        sessionDto.setTeacher_id(7L);
        sessionDto.setUsers(Arrays.asList(9L, 3L, 4L));
        Teacher teacher = new Teacher();
        teacher.setId(7L);
        when(teacherService.findById(7L)).thenReturn(teacher);

        Session session = sessionMapper.toEntity(sessionDto);

        verify(teacherService).findById(7L);
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId()); //?
        assertEquals(sessionDto.getUsers(), session.getUsers());
    }
}