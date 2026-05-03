package com.uade.tpo.demo.repository;

import com.uade.tpo.demo.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Page<Producto> findByActivoTrue(Pageable pageable);

    Page<Producto> findByEquipoIdAndActivoTrue(Long equipoId, Pageable pageable);

    Page<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId, Pageable pageable);

    Page<Producto> findByPrecioBetweenAndActivoTrue(Double precioMin, Double precioMax, Pageable pageable);

    Optional<Producto> findByIdAndActivoTrue(Long id);
}
