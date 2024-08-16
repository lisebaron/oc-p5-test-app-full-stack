package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService; // service under test

    @BeforeEach
    void setUp() {
        this.teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void findById_shouldFindTeacherById() {
        Long teacherId = 1L;
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.findById(teacherId);

        verify(teacherRepository).findById(teacherId);
        assertEquals(teacher, result);
    }

    @Test
    void findAll_shouldFindAllTeacher() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();
        Teacher teacher3 = new Teacher();
        List<Teacher> expectedResult = Arrays.asList(teacher1, teacher2, teacher3);
        when(teacherRepository.findAll()).thenReturn(expectedResult);

        List<Teacher> result = teacherService.findAll();

        verify(teacherRepository).findAll();
        assertEquals(expectedResult, result);
    }
}