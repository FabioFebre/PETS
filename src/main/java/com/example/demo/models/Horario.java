package com.example.demo.models;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "api_horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horario_id")
    private Long horarioId;

    @Column(name = "hora", unique = true, nullable = false)
    private LocalTime hora;

    public Horario() {}

    public Long getHorarioId() {
        return horarioId;
    }

    public void setHorarioId(Long horarioId) {
        this.horarioId = horarioId;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return hora.toString();
    }
}
