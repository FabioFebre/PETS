package com.example.demo.models;


import jakarta.persistence.*;

@Entity
@Table(name = "api_categoriaproducto")
public class CategoriaProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_producto_id")
    private Long categoriaProductoId;

    @Column(name = "nombre", length = 100, unique = true, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT", nullable = true)
    private String descripcion;

    // Getters y Setters
    public Long getCategoriaProductoId() {
        return categoriaProductoId;
    }

    public void setCategoriaProductoId(Long categoriaProductoId) {
        this.categoriaProductoId = categoriaProductoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }

}
