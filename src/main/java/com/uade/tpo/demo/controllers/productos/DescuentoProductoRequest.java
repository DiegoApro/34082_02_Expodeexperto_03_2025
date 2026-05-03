package com.uade.tpo.demo.controllers.productos;

import lombok.Data;

@Data
public class DescuentoProductoRequest {
    private Long descuentoId; // null para quitar el descuento
}
