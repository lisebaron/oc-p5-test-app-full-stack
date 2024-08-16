package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        this.sessionController = new SessionController(sessionService, sessionMapper);
    }

    @Test
    void findById_shouldFindSessionById() {
        Session session = new Session();
        when(sessionService.getById(7L)).thenReturn(session);

        sessionController.findById(String.valueOf(7));


    }

}