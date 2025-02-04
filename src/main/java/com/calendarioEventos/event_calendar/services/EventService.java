package com.calendarioEventos.event_calendar.services;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.EventDTO;
import com.calendarioEventos.event_calendar.entities.Event;
import com.calendarioEventos.event_calendar.entities.User;
import com.calendarioEventos.event_calendar.repository.EventRepository;
import com.calendarioEventos.event_calendar.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Component
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    private LocalDateTime converterData(String data) {
        try {
            return LocalDateTime.parse(data);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de data e hora invalido. Use o seguinte padrão: yyyy-MM-dd'T'HH:mm:ss");
        }
    }

    private boolean verificaConflitoDeHorario(Event novoEvento, User user) {
        List<Event> eventosDoUser = user.getEventos();

        for(Event e : eventosDoUser) {
            if(novoEvento.getHoraInicio().isBefore(e.getHoraTermino()) &&
            novoEvento.getHoraTermino().isAfter(e.getHoraInicio())) {
                return true;
            }
        }
        return false;
    }

    public void createEvent(@RequestBody EventDTO dto, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        Event event = new Event();
        event.setUsuario(user.get());
        event.setDescricao(dto.descricao());
        System.out.println(dto.descricao());
        System.out.println(dto.horaInicio());
        System.out.println(dto.horaTermino());
        event.setHoraInicio(converterData(dto.horaInicio()));
        event.setHoraTermino(converterData(dto.horaTermino()));

        //verificacao de nao sobrescrita
        if(verificaConflitoDeHorario(event, user.get()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Conflito de Horarios");
        else
            eventRepository.save(event);
    }

    public void deleteEventById(UUID eventId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado"));

        if(!event.getUsuario().getId().equals(user.get().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario logado é diferente do usuário que o evento pertence");

        eventRepository.delete(event);
    }

    public void updateEvent(UUID eventId, @RequestBody EventDTO dto, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado"));

        if(!event.getUsuario().getId().equals(user.get().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario logado é diferente do usuário que o evento pertence");

        if(!dto.horaInicio().isBlank())
            event.setHoraInicio(converterData(dto.horaInicio()));
        if(!dto.horaTermino().isBlank())
            event.setHoraTermino(converterData(dto.horaTermino()));
        if(!dto.descricao().isBlank())
            event.setDescricao(dto.descricao());

        eventRepository.save(event);
    }

    public Event getEventByID(UUID eventId, JwtAuthenticationToken token) {
        var user = userRepository.findById(UUID.fromString(token.getName()));
        if (user.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento nao encontrado"));

        if(!event.getUsuario().getId().equals(user.get().getId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario logado é diferente do usuário que o evento pertence");

        return event;
    }
}
