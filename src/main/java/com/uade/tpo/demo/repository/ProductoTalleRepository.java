package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.ProductoTalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductoTalleRepository extends JpaRepository<ProductoTalle, Long> {
    Optional<ProductoTalle> findByProductoIdAndTalleId(Long productoId, Long talleId);
}