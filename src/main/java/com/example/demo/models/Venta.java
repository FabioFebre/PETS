package com.example.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "api_venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id", nullable = false)
    private MetodoPago metodoPago;

    @Column(name = "numero_tarjeta", length = 16)
    private String numeroTarjeta;

    @Column(name = "correo")
    private String correo;

    @Column(name = "fecha_expiracion")
    private LocalDate fechaExpiracion;

    @Column(name = "cvv", length = 3)
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;



    @Column(name = "fecha_venta", updatable = false)
    private LocalDateTime fechaCompra = LocalDateTime.now();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

    public Venta() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }


    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra; // Asegúrate de incluir el getter para fechaCompra
    }


    @Override
    public String toString() {
        return "Transaccion " + id + " - Usuario: " + usuario.getUsername();
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}