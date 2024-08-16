package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService; // service under test

    @BeforeEach
    void setUp() {
        this.sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    void create_shouldCreateSession() {
        Session session = new Session();
        when(sessionRepository.save(session)).thenReturn(session);

        Session sessionCreated = sessionService.create(session);

        verify(sessionRepository).save(session);
        assertNotNull(sessionCreated);
        assertEquals(session, sessionCreated);
    }

    @Test
    void delete_shouldDeleteSession() {
        Long sessionId = 4L;

        sessionService.delete(sessionId);

        verify(sessionRepository).deleteById(4L);
    }

    @Test
    void findAll_shouldGetAllSessions() {
        Session session1 = new Session();
        Session session2 = new Session();
        Session session3 = new Session();
        List<Session> expectedResult = Arrays.asList(session1, session2, session3);
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2, session3));

        List<Session> result = sessionService.findAll();

        verify(sessionRepository).findAll();
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void getById_shouldGetSessionById() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        verify(sessionRepository).findById(1L);
        assertNotNull(result);
        assertEquals(session, result);
    }

    @Test
    void update_shouldUpdateSessionById() {
        Session session = new Session();
        Long id = 7L;
        session.setId(id);
        when(sessionRepository.save(session)).thenReturn(session);

        Session result = sessionService.update(id, session);

        verify(sessionRepository).save(session);
        assertNotNull(result);
        assertEquals(session, result);
    }

    @Test
    void participate_shouldAddUserInSessionUserList() {
        Long sessionId = 5L;
        Long userId = 7L;
        Session session = new Session();
        session.setId(sessionId);
        session.setUsers(new ArrayList<User>());
        User user = new User().setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        sessionService.participate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
        verify(sessionRepository).save(session);
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    void participate_shouldThrowNotFoundException_whenSessionAndUserAreNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> {
                    sessionService.participate(1L, 1L);
                });
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void participate_shouldThrowNotFoundException_whenSessionIsNotFound() {
        User user = new User();
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(NotFoundException.class,
                () -> {
                    sessionService.participate(1L, 1L);
                });
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void participate_shouldThrowNotFoundException_whenUserIsNotFound() {
        Session session = new Session();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> {
                    sessionService.participate(1L, 1L);
                });
        verify(sessionRepository).findById(1L);
        verify(userRepository).findById(1L);
    }

    @Test
    void participate_shouldThrowBadRequestException_whenUserAlreadyParticipate() {
        Long sessionId = 5L;
        Long userId = 7L;
        Session session = new Session().setId(sessionId);
        User user = new User().setId(userId);
        session.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.ofNullable(session));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        assertThrows(BadRequestException.class,
                () -> {
                    sessionService.participate(sessionId, userId);
                });
        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId);
    }

    @Test
    void noLongerParticipate_shouldRemoveUserInSessionUserList() {
        Long sessionId = 5L;
        Long userId = 7L;
        Session session = new Session();
        session.setId(sessionId);
        User user = new User().setId(userId);
        session.setUsers(Arrays.asList(user));

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(sessionId, userId);

        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(session);
        assertFalse(session.getUsers().contains(user));
    }

    @Test
    void noLongerParticipate_shouldThrowNotFoundException_whenSessionIsNotFound() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,
                () -> {
                    sessionService.noLongerParticipate(1L, 1L);
                });
        verify(sessionRepository).findById(1L);
    }

    @Test
    void noLongerParticipate_shouldThrowBadRequestException_whenUserDoesNotParticipate() {
        Long sessionId = 5L;
        Long userId = 7L;
        Session session = new Session().setId(sessionId);
        session.setUsers(new ArrayList<User>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class,
                () -> {
                    sessionService.noLongerParticipate(sessionId, userId);
                });
        verify(sessionRepository).findById(sessionId);
    }
}
