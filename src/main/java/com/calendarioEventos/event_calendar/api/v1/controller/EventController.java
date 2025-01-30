package com.calendarioEventos.event_calendar.api.v1.controller;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.EventDTO;
import com.calendarioEventos.event_calendar.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Eventos")
@RestController
@RequestMapping("/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Criacao de evento")
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Void> createEvent(@RequestBody EventDTO dto, JwtAuthenticationToken token) {
        eventService.createEvent(dto, token);
        return ResponseEntity.ok().build();
    }
}
