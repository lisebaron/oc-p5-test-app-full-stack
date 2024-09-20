package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TeacherMapperImplTest {

    private TeacherMapperImpl teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    void toDto_shouldConvertEntityToDto() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
    }

    @Test
    void toEntityList_shouldConvertDtoListToEntityList() {
        TeacherDto teacherDtoOne = new TeacherDto();
        teacherDtoOne.setId(1L);
        TeacherDto teacherDtoTwo = new TeacherDto();
        teacherDtoTwo.setId(2L);
        List<TeacherDto> teacherDtoList = List.of(teacherDtoOne, teacherDtoTwo);

        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);

        assertThat(teacherList).hasSize(2);
        assertThat(teacherList.get(0).getId()).isEqualTo(teacherDtoOne.getId());
        assertThat(teacherList.get(1).getId()).isEqualTo(teacherDtoTwo.getId());
    }

    @Test
    void toDtoList_shouldConvertEntityListToDtoList() {
        Teacher teacherOne = new Teacher();
        teacherOne.setId(1L);
        Teacher teacherTwo = new Teacher();
        teacherTwo.setId(2L);
        List<Teacher> teacherList = List.of(teacherOne, teacherTwo);

        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);

        assertThat(teacherDtoList).hasSize(2);
        assertThat(teacherDtoList.get(0).getId()).isEqualTo(teacherOne.getId());
        assertThat(teacherDtoList.get(1).getId()).isEqualTo(teacherTwo.getId());
    }
}