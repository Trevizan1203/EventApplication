package com.calendarioEventos.event_calendar.api.v1.controller;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.CreateUser;
import com.calendarioEventos.event_calendar.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Criacao de usuario")
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<Void> createUser(@RequestBody CreateUser dto) {
        userService.createUser(dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delecao de usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/delete")
    @Transactional
    public ResponseEntity<Void> deleteUser (@RequestBody CreateUser dto, JwtAuthenticationToken token) {
        userService.deleteUser(token, dto);
        return ResponseEntity.ok().build();
    }
}
