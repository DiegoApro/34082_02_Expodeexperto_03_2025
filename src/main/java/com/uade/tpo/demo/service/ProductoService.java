package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.*;
import com.uade.tpo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final EquipoRepository equipoRepository;
    private final CategoryRepository categoriaRepository;
    private final TalleRepository talleRepository;
    private final ProductoTalleRepository productoTalleRepository;
    
    public Page<Producto> getProductos(PageRequest pageRequest) {
        return productoRepository.findAll(pageRequest);
    }
    
    public Page<Producto> getProductosByEquipo(Long equipoId, PageRequest pageRequest) {
        return productoRepository.findByEquipoId(equipoId, pageRequest);
    }
    
    public Page<Producto> getProductosByCategoria(Long categoriaId, PageRequest pageRequest) {
        return productoRepository.findByCategoriaId(categoriaId, pageRequest);
    }
    
    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findById(id);
    }
    
    @Transactional
    public Producto createProducto(Producto producto, Long equipoId, Long categoriaId) {
        Equipo equipo = equipoRepository.findById(equipoId)
            .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        producto.setEquipo(equipo);
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }
    
    @Transactional
    public ProductoTalle addStockToProducto(Long productoId, Long talleId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Talle talle = talleRepository.findById(talleId)
            .orElseThrow(() -> new RuntimeException("Talle no encontrado"));
        
        Optional<ProductoTalle> existing = productoTalleRepository.findByProductoIdAndTalleId(productoId, talleId);
        
        if (existing.isPresent()) {
            existing.get().setStock(existing.get().getStock() + cantidad);
            return productoTalleRepository.save(existing.get());
        } else {
            ProductoTalle pt = new ProductoTalle();
            pt.setProducto(producto);
            pt.setTalle(talle);
            pt.setStock(cantidad);
            return productoTalleRepository.save(pt);
        }
    }
}