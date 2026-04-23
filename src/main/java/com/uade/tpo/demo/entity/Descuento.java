package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "descuentos")
public class Descuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Double porcentaje;  // ¡IMPORTANTE! Ej: 10.0 = 10% de descuento
    
    @Column(name = "codigo", unique = true)
    private String codigo;  // Código promocional (ej: "VERANO2024")
    
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    private String descripcion;
    
    // Relación con Productos
    @OneToMany(mappedBy = "descuento")
    private List<Producto> productos;
    
    // Relación con Ordenes
    @OneToMany(mappedBy = "descuento")
    private List<Pedido> pedidos;
}