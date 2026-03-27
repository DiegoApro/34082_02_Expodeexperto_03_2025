package com.uade.tpo.demo.controllers.pedidos;

import lombok.Data;

@Data
public class PedidoItemRequest {
    private Long productoTalleId;  // ID del producto con talle específico
    private Integer cantidad;
}