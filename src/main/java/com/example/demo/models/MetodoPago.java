package com.example.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "api_metodopago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_id")
    private Long metodo_id;

    @Column(name = "nombre", length = 20, unique = true, nullable = false)
    private String nombre;

    public MetodoPago() {}


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

    public Long getMetodo_id() {
        return metodo_id;
    }

    public void setMetodo_id(Long metodo_id) {
        this.metodo_id = metodo_id;
    }
}
