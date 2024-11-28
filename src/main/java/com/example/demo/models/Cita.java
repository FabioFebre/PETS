package com.example.demo.models;

import com.example.demo.models.Mascota;
import com.example.demo.models.Servicio;
import com.example.demo.models.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "api_cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cita_id")
    private Long citaId;////

    @ManyToOne
    @JoinColumn(name = "usuario_id_id", nullable = true)
    private Usuario usuario;///////////

    @ManyToOne
    @JoinColumn(name = "mascota_id_id", nullable = false)
    private Mascota mascota;////////////

    @ManyToOne
    @JoinColumn(name = "servicio_id_id", nullable = false)
    private Servicio servicio;////////////

    @Column(name = "razon", columnDefinition = "TEXT")
    private String razon;//////////////

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;/////////////////

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;////////////////

    @ManyToOne
    @JoinColumn(name = "horario_id_id", nullable = false)
    private Horario horario;///////////

    @Column(name = "costo_cita", precision = 10, scale = 2, nullable = false)
    private BigDecimal costoCita;/////////////////

    @Column(name = "estado", nullable = false)
    private Boolean estado = false;///////////////

    public Cita() {}

    @PrePersist
    @PreUpdate
    public void calcularCostoCita() {
        if (this.servicio != null) {
            this.costoCita = this.servicio.getPrecio();
        }
    }

    // Getters y setters

    public Long getCitaId() {
        return citaId;
    }

    public void setCitaId(Long citaId) {
        this.citaId = citaId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public BigDecimal getCostoCita() {
        return costoCita;
    }

    public void setCostoCita(BigDecimal costoCita) {
        this.costoCita = costoCita;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cita para " + mascota.getNombre() + " el " + fechaCita + " a las " + horario.getHora();
    }
}
