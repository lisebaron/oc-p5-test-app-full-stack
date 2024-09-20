package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    private TeacherController controller;

    @BeforeEach
    public void setUp() {
        controller = new TeacherController(teacherService, teacherMapper);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void findById_shouldFindTeacherById() throws Exception {
        final var id = 1L;
        Teacher teacher = new Teacher();
        teacher.setId(id);

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(id);

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        final var result = mockMvc.perform(get("/api/teacher/" + id));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$").exists());
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        final var id = 1L;
        when(teacherService.findById(id)).thenReturn(null);

        final var result = mockMvc.perform(get("/api/teacher/" + id));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findById_shouldReturnBadRequest() throws Exception {
        final var result = mockMvc.perform(get("/api/teacher/InvalidId"));

        result.andExpect(status().isBadRequest());
    }

    @Test
    void findAll_shouldFindAllTeachers() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();
        List<Teacher> teacherList = List.of(teacher);
        List<TeacherDto> teacherDtoList = List.of(teacherDto);

        when(teacherService.findAll()).thenReturn(teacherList);
        when(teacherMapper.toDto(teacherList)).thenReturn(teacherDtoList);

        final var result = mockMvc.perform(get("/api/teacher"));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        result.andExpect(jsonPath("$[0]").exists());
    }
}