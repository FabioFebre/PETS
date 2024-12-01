package com.example.demo.repository;


import com.example.demo.models.DetalleCarrito;
import com.example.demo.models.Carrito;
import com.example.demo.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {
    Optional<DetalleCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
    List<DetalleCarrito> findByCarritoId(Long carritoId);

    List<DetalleCarrito> findByCarrito(Carrito carrito);

}