package com.calendarioEventos.event_calendar.api.v1.controller;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.LoginRequest;
import com.calendarioEventos.event_calendar.api.v1.controller.DTO.LoginResponse;
import com.calendarioEventos.event_calendar.services.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Login")
@RestController
@RequestMapping("/v1/auth")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(tokenService.login(loginRequest));
    }

}
