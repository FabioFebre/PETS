package com.example.demo.repository;


import com.example.demo.models.ProductoCarrito;
import com.example.demo.models.Carrito;
import com.example.demo.models.Producto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductoCarritoRepository extends JpaRepository<ProductoCarrito, Long> {

    List<ProductoCarrito> findByCarrito(Carrito carrito);

    Optional<ProductoCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
    @Modifying
    @Query("DELETE FROM ProductoCarrito pc WHERE pc.carrito.id = :carritoId AND pc.producto.id = :productoId")
    void deleteByCarritoIdAndProductoId(Long carritoId, Long productoId);

    @Modifying
    @Query("DELETE FROM ProductoCarrito pc WHERE pc.carrito.id = :carritoId")
    void deleteByCarritoId(Long carritoId);

}
