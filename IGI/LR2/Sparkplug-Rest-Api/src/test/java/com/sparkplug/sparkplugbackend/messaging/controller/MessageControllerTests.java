package com.sparkplug.sparkplugbackend.messaging.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparkplug.sparkplugbackend.messaging.service.MessagesService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MessageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MessagesService messagesService;

    @Autowired
    private ObjectMapper objectMapper;


}
