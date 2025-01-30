package com.calendarioEventos.event_calendar.api.v1.controller;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.EventDTO;
import com.calendarioEventos.event_calendar.services.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @Operation(summary = "Delecao de evento")
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<Void> deleteEvent(UUID eventId, JwtAuthenticationToken token) {
        eventService.deleteEventById(eventId, token);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualizacao de evento")
    @PutMapping("/update")
    @Transactional
    public ResponseEntity<Void> updateEvent(UUID eventId,@RequestBody EventDTO dto, JwtAuthenticationToken token) {
        eventService.updateEvent(eventId, dto, token);
        return ResponseEntity.noContent().build();
    }


}
