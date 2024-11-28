package com.example.demo.models;


import jakarta.persistence.*;

@Entity
@Table(name = "api_categoriaservicio")
public class CategoriaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_servicio_id")
    private Long categoriaId;  // Campo clave primaria

    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Getters y setters
    public Long getCategoriaServicioId() {
        return categoriaId;
    }

    public void setCategoriaServicioId(Long categoriaServicioId) {
        this.categoriaId = categoriaServicioId;
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

    // Método toString()
    @Override
    public String toString() {
        return "CategoriaServicio{" +
                "categoriaServicioId=" + categoriaId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
