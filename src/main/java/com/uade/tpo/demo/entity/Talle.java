package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "talles")
public class Talle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre; // S, M, L, XL, etc.
    
    @OneToMany(mappedBy = "talle")
    private List<ProductoTalle> productoTalles;
}