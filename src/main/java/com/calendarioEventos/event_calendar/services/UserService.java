package com.calendarioEventos.event_calendar.services;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.UserDTO;
import com.calendarioEventos.event_calendar.entities.Event;
import com.calendarioEventos.event_calendar.entities.User;
import com.calendarioEventos.event_calendar.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(@RequestBody @NotNull UserDTO dto) {

        var userFromDB = userRepository.findByUsername(dto.username());

        if (userFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja existente");
        }

        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(newUser);
    }

    public void deleteUser(@RequestBody @NotNull UserDTO dto) {
        var userFromDB = userRepository.findByUsername(dto.username());
        if (userFromDB.isPresent()) {
            if(userFromDB.get().getUsername().equalsIgnoreCase(dto.username()) && passwordEncoder.matches(dto.password(), userFromDB.get().getPassword())) {
                userRepository.deleteById(userFromDB.get().getId());
            }
            else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciais Inválidas");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario nao encontrado no banco de dados");
        }
    }

    public List<Event> findAllEventsByUser(JwtAuthenticationToken token) {
        var userFromDB = userRepository.findById(UUID.fromString(token.getName()));
        if (userFromDB.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        }
        return userFromDB.get().getEventos();
    }


}
