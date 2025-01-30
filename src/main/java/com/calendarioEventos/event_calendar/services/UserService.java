package com.calendarioEventos.event_calendar.services;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.CreateUser;
import com.calendarioEventos.event_calendar.entities.User;
import com.calendarioEventos.event_calendar.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Void> createUser(@RequestBody CreateUser dto) {

        var userFromDB = userRepository.findByUsername(dto.username());

        if (userFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja existente");
        }

        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setPassword(passwordEncoder.encode(dto.password()));

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }



}
