package com.calendarioEventos.event_calendar.api.v1.controller;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.LoginRequest;
import com.calendarioEventos.event_calendar.api.v1.controller.DTO.LoginResponse;
import com.calendarioEventos.event_calendar.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Tag(name = "Login")
@RestController
@RequestMapping("/v1/auth")
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var user = userRepository.findByUsername(loginRequest.username());

        if(user.isEmpty())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario não encontrado");
        if(!user.get().isLoginCorrect(loginRequest, passwordEncoder))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Senha incorreta");

        var now = Instant.now();
        var expiresIn = 300L; // 5 minutos

        var claims = JwtClaimsSet.builder()
                .issuer("myBackend")//quem esta gerando o token
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

}
