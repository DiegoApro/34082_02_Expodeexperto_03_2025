package com.uade.tpo.demo.controllers.equipos;

import com.uade.tpo.demo.entity.Equipo;
import com.uade.tpo.demo.repository.EquipoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {
    private final EquipoRepository equipoRepository;

    @GetMapping
    public ResponseEntity<List<Equipo>> getEquipos() {
        return ResponseEntity.ok(equipoRepository.findAll());
    }
}
