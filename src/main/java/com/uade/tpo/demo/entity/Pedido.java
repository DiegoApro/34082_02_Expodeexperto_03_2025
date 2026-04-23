package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    @Column(nullable = false)
    private String estado; // PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Pago pago;

    @ManyToOne
@JoinColumn(name = "descuento_id")
private Descuento descuento;  

public Double getSubtotal() {
    return detalles.stream()
        .mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad())
        .sum();
}

public Double getTotal() {
    Double subtotal = getSubtotal();
    if (descuento != null && descuento.getActivo()) {
        return subtotal - (subtotal * descuento.getPorcentaje() / 100);
    }
    return subtotal;
}
}