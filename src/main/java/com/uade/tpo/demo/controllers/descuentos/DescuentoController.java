package com.uade.tpo.demo.controllers.descuentos;

import com.uade.tpo.demo.entity.Descuento;
import com.uade.tpo.demo.repository.DescuentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/descuentos")
@RequiredArgsConstructor
public class DescuentoController {

    private final DescuentoRepository descuentoRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Descuento>> getDescuentos() {
        return ResponseEntity.ok(descuentoRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Descuento> getDescuentoById(@PathVariable Long id) {
        return descuentoRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
