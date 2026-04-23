package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
    Optional<Descuento> findByCodigo(String codigo);
    List<Descuento> findByActivoTrue();
}