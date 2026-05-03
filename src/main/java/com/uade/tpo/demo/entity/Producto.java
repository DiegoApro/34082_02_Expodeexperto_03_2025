package com.uade.tpo.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
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
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url")
    private List<String> imagenesUrl = new ArrayList<>();

    @Column(nullable = false)
    private boolean activo = true;

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