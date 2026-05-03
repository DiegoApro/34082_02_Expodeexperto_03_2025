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
    private final DescuentoRepository descuentoRepository;

    public Page<Producto> getProductos(PageRequest pageRequest) {
        return productoRepository.findByActivoTrue(pageRequest);
    }

    public Page<Producto> getProductosByEquipo(Long equipoId, PageRequest pageRequest) {
        return productoRepository.findByEquipoIdAndActivoTrue(equipoId, pageRequest);
    }

    public Page<Producto> getProductosByCategoria(Long categoriaId, PageRequest pageRequest) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId, pageRequest);
    }

    public Page<Producto> getProductosByPrecio(Double precioMin, Double precioMax, PageRequest pageRequest) {
        double min = precioMin != null ? precioMin : 0.0;
        double max = precioMax != null ? precioMax : Double.MAX_VALUE;
        return productoRepository.findByPrecioBetweenAndActivoTrue(min, max, pageRequest);
    }

    public Optional<Producto> getProductoById(Long id) {
        return productoRepository.findByIdAndActivoTrue(id);
    }

    @Transactional
    public Producto createProducto(Producto producto, Long equipoId, Long categoriaId) {
        Equipo equipo = equipoRepository.findById(equipoId)
            .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setEquipo(equipo);
        producto.setCategoria(categoria);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto updateProducto(Long id, Producto datos, Long equipoId, Long categoriaId) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Equipo equipo = equipoRepository.findById(equipoId)
            .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        Categoria categoria = categoriaRepository.findById(categoriaId)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setImagenesUrl(datos.getImagenesUrl());
        producto.setEquipo(equipo);
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    @Transactional
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional
    public Producto asignarDescuento(Long productoId, Long descuentoId) {
        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        Descuento descuento = descuentoRepository.findById(descuentoId)
            .orElseThrow(() -> new RuntimeException("Descuento no encontrado"));
        producto.setDescuento(descuento);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto quitarDescuento(Long productoId) {
        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setDescuento(null);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto agregarImagen(Long productoId, String url) {
        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.getImagenesUrl().add(url);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto eliminarImagen(Long productoId, String url) {
        Producto producto = productoRepository.findByIdAndActivoTrue(productoId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.getImagenesUrl().remove(url);
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
