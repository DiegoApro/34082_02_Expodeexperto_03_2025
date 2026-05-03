package com.uade.tpo.demo.controllers.productos;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductoRequest {
    private String nombre;
    private String descripcion;
    private Double precio;
    private Long equipoId;
    private Long categoriaId;
    private List<String> imagenesUrl = new ArrayList<>();
}
