package com.uade.tpo.demo.controllers.productos;

import com.uade.tpo.demo.entity.Producto;
import com.uade.tpo.demo.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductosController {
    private final ProductoService productoService;
    
    @GetMapping
    public ResponseEntity<Page<Producto>> getProductos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) Long categoriaId) {
        
        PageRequest pageRequest = (page != null && size != null) 
            ? PageRequest.of(page, size) 
            : PageRequest.of(0, 100);
        
        if (equipoId != null) {
            return ResponseEntity.ok(productoService.getProductosByEquipo(equipoId, pageRequest));
        } else if (categoriaId != null) {
            return ResponseEntity.ok(productoService.getProductosByCategoria(categoriaId, pageRequest));
        } else {
            return ResponseEntity.ok(productoService.getProductos(pageRequest));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImagenUrl(request.getImagenUrl());
        
        Producto result = productoService.createProducto(producto, request.getEquipoId(), request.getCategoriaId());
        return ResponseEntity.created(URI.create("/api/productos/" + result.getId())).body(result);
    }
    
    @PostMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addStock(@PathVariable Long id, @RequestBody StockRequest request) {
        productoService.addStockToProducto(id, request.getTalleId(), request.getCantidad());
        return ResponseEntity.ok().build();
    }
}