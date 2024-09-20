package com.openclassrooms.starterjwt.mapper;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void toEntity_shouldConvertDtoToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("john.doe@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setPassword("password");
        userDto.setAdmin(true);

        User user = userMapper.toEntity(userDto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userDto.getId());
        assertThat(user.getEmail()).isEqualTo(userDto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(userDto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(userDto.getLastName());
        assertThat(user.getPassword()).isEqualTo(userDto.getPassword());
        assertThat(user.isAdmin()).isEqualTo(userDto.isAdmin());
    }

    @Test
    void toDto_shouldConvertEntityToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setAdmin(true);

        UserDto userDto = userMapper.toDto(user);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isEqualTo(user.getId());
        assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(userDto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(userDto.getLastName()).isEqualTo(user.getLastName());
        assertThat(userDto.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDto.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    void toEntityList_shouldConvertDtoListToEntityList() {
        UserDto userDtoOne = new UserDto();
        userDtoOne.setId(1L);
        userDtoOne.setEmail("john.doe@example.com");
        userDtoOne.setFirstName("John");
        userDtoOne.setLastName("Doe");
        userDtoOne.setPassword("password");

        UserDto userDtoTwo = new UserDto();
        userDtoTwo.setId(2L);
        userDtoTwo.setEmail("another.doe@example.com");
        userDtoTwo.setFirstName("Another");
        userDtoTwo.setLastName("Doe");
        userDtoTwo.setPassword("password");

        List<UserDto> dtoList = List.of(userDtoOne, userDtoTwo);

        List<User> userList = userMapper.toEntity(dtoList);

        assertThat(userList).hasSize(2);
        assertThat(userList.get(0).getId()).isEqualTo(userDtoOne.getId());
        assertThat(userList.get(1).getId()).isEqualTo(userDtoTwo.getId());
    }

    @Test
    void toDtoList_shouldConvertEntityListToDtoList() {
        User userOne = new User();
        userOne.setEmail("john.doe@example.com");
        userOne.setFirstName("John");
        userOne.setLastName("Doe");
        userOne.setPassword("password");

        User userTwo = new User();
        userTwo.setId(2L);
        userTwo.setEmail("another.doe@example.com");
        userTwo.setFirstName("Another");
        userTwo.setLastName("Doe");
        userTwo.setPassword("password");

        List<User> userList = List.of(userOne, userTwo);

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertThat(userDtoList).hasSize(2);
        assertThat(userDtoList.get(0).getId()).isEqualTo(userOne.getId());
        assertThat(userDtoList.get(1).getId()).isEqualTo(userTwo.getId());
    }
}

