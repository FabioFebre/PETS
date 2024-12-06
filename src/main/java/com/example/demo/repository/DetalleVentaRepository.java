package com.example.demo.repository;

import com.example.demo.models.DetalleVenta;
import com.example.demo.models.Usuario;
import com.example.demo.models.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaUsuario(Usuario usuario);
    List<DetalleVenta> findByVenta(@Param("venta") Venta venta);

}