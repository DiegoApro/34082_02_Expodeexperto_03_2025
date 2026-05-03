package com.uade.tpo.demo.controllers.talles;

import com.uade.tpo.demo.entity.Talle;
import com.uade.tpo.demo.repository.TalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/talles")
@RequiredArgsConstructor
public class TalleController {
    private final TalleRepository talleRepository;

    @GetMapping
    public ResponseEntity<List<Talle>> getTalles() {
        return ResponseEntity.ok(talleRepository.findAll());
    }
}
