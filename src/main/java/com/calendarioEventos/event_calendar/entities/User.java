package com.calendarioEventos.event_calendar.entities;

import com.calendarioEventos.event_calendar.api.v1.controller.DTO.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usuario_id")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    @OneToMany(mappedBy = "usuario")
    private List<Event> eventos = new ArrayList<Event>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password);
    }
}
