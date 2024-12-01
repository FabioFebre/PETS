package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "api_metodopago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_id")
    private Long metodoId;

    @Column(name = "nombre", length = 20, unique = true, nullable = false)
    private String nombre;

    public MetodoPago() {}

    public Long getMetodoId() {
        return metodoId;
    }

    public void setMetodoId(Long metodoId) {
        this.metodoId = metodoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
