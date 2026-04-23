package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private Double precio;
    
    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ProductoTalle> productoTalles;
    
    @Column
    private String imagenUrl; // Para almacenar URL de la imagen del producto

    @ManyToOne
    @JoinColumn(name = "descuento_id")
    private Descuento descuento;  

//método para calcular precio con descuento
public Double getPrecioFinal() {
    if (descuento != null && descuento.getActivo()) {
        return precio - (precio * descuento.getPorcentaje() / 100);
    }
    return precio;
}
}