package com.example.demo.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "api_carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "carrito")
    @JsonManagedReference
    private List<ProductoCarrito> productoCarrito = new ArrayList<>();  // Inicialización explícita

    @OneToMany(mappedBy = "carrito")
    private List<DetalleVenta> detalleVenta = new ArrayList<>(); // Para almacenar detalles específicos del carrito

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    // Constructor vacío
    public Carrito() {}

    public List<DetalleVenta> getDetalles() {
        return this.detalleVenta;
    }


    @Override
    public String toString() {
        return "Carrito de usuario: " + usuario.getUsername() + " (ID: " + id + ")";
    }
}
