package com.uade.tpo.demo.controllers.productos;

import lombok.Data;

@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long equipoId;
    private Long categoriaId;
    private String imagenUrl;
}
