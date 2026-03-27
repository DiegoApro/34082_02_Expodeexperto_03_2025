package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String metodo; // TARJETA_CREDITO, TARJETA_DEBITO, MERCADO_PAGO, etc.
    
    @Column(nullable = false)
    private Double monto;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(nullable = false)
    private String estado; // PENDIENTE, COMPLETADO, RECHAZADO
    
    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}