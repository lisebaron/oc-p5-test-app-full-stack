package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private UserDetailsImpl userDetailsOne;
    private UserDetailsImpl userDetailsTwo;

    @BeforeEach
    public void setUp() {
        userDetailsOne = new UserDetailsImpl(1L, "testUser", "Test", "User", false, "password");
        userDetailsTwo = new UserDetailsImpl(2L, "anotherUser", "Another", "User", true, "password");
    }

    @Test
    void getAuthorities_shouldReturnEmptyCollection() {
        Collection<? extends GrantedAuthority> authorities = userDetailsOne.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        assertTrue(userDetailsOne.isAccountNonExpired());
        assertTrue(userDetailsTwo.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        assertTrue(userDetailsOne.isAccountNonLocked());
        assertTrue(userDetailsTwo.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        assertTrue(userDetailsOne.isCredentialsNonExpired());
        assertTrue(userDetailsTwo.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        assertTrue(userDetailsOne.isEnabled());
        assertTrue(userDetailsTwo.isEnabled());
    }

    @Test
    void equals_shouldReturnTrue_whenSameId() {
        UserDetailsImpl userDetailsCopy = new UserDetailsImpl(1L, "testUserCopy", "Test", "User", false, "password");
        assertEquals(userDetailsOne, userDetailsCopy);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentId() {
        assertNotEquals(userDetailsOne, userDetailsTwo);
    }

    @Test
    void equals_shouldReturnFalse_whenNullOrDifferentClass() {
        assertNotEquals(userDetailsOne, null);
        assertNotEquals(userDetailsOne, new Object());
    }

    @Test
    void getUsername_shouldReturnCorrectUsername() {
        assertEquals("testUser", userDetailsOne.getUsername());
        assertEquals("anotherUser", userDetailsTwo.getUsername());
    }

    @Test
    void getFirstName_shouldReturnCorrectFirstName() {
        assertEquals("Test", userDetailsOne.getFirstName());
        assertEquals("Another", userDetailsTwo.getFirstName());
    }

    @Test
    void getLastName_shouldReturnCorrectLastName() {
        assertEquals("User", userDetailsOne.getLastName());
        assertEquals("User", userDetailsTwo.getLastName());
    }

    @Test
    void getPassword_shouldReturnCorrectPassword() {
        assertEquals("password", userDetailsOne.getPassword());
        assertEquals("password", userDetailsTwo.getPassword());
    }

    @Test
    void isAdmin_shouldReturnCorrectAdminStatus() {
        assertFalse(userDetailsOne.getAdmin());
        assertTrue(userDetailsTwo.getAdmin());
    }
}

