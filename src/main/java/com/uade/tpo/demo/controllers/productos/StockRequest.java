package com.uade.tpo.demo.controllers.productos;

import lombok.Data;

@Data
public class StockRequest {
    private Long talleId;
    private Integer cantidad;
}