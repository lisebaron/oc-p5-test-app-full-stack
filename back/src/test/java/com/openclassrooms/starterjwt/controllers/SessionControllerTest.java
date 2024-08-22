package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SessionController.class)
class SessionControllerTest {

    //wip
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SessionService sessionService;
//
//    @MockBean
//    private SessionMapper sessionMapper;
//
//    @MockBean
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Mock
//    private Session session;
//
//    @Test
//    void findById_shouldFindSessionById() throws Exception {
//        when(sessionService.getById(7L)).thenReturn(session);
//
//        mockMvc.perform(MockMvcRequestBuilders.get(
//                "/api/session/7"
//        )).andExpect(MockMvcResultMatchers.status().isOk());
//    }

}