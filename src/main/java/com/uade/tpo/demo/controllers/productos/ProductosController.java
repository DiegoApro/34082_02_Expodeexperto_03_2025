package com.uade.tpo.demo.controllers.productos;

import com.uade.tpo.demo.entity.Producto;
import com.uade.tpo.demo.service.FileStorageService;
import com.uade.tpo.demo.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductosController {
    private final ProductoService productoService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<Page<Producto>> getProductos(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {

        PageRequest pageRequest = (page != null && size != null)
            ? PageRequest.of(page, size)
            : PageRequest.of(0, 100);

        if (equipoId != null) {
            return ResponseEntity.ok(productoService.getProductosByEquipo(equipoId, pageRequest));
        } else if (categoriaId != null) {
            return ResponseEntity.ok(productoService.getProductosByCategoria(categoriaId, pageRequest));
        } else if (precioMin != null || precioMax != null) {
            return ResponseEntity.ok(productoService.getProductosByPrecio(precioMin, precioMax, pageRequest));
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
        producto.setImagenesUrl(request.getImagenesUrl());

        Producto result = productoService.createProducto(producto, request.getEquipoId(), request.getCategoriaId());
        return ResponseEntity.created(URI.create("/api/productos/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Producto datos = new Producto();
        datos.setNombre(request.getNombre());
        datos.setDescripcion(request.getDescripcion());
        datos.setPrecio(request.getPrecio());
        datos.setImagenesUrl(request.getImagenesUrl());

        Producto result = productoService.updateProducto(id, datos, request.getEquipoId(), request.getCategoriaId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/descuento")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> gestionarDescuento(
            @PathVariable Long id,
            @RequestBody DescuentoProductoRequest request) {
        if (request.getDescuentoId() == null) {
            return ResponseEntity.ok(productoService.quitarDescuento(id));
        }
        return ResponseEntity.ok(productoService.asignarDescuento(id, request.getDescuentoId()));
    }

    @PostMapping("/{id}/imagenes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> subirImagen(
            @PathVariable Long id,
            @RequestParam("archivo") MultipartFile archivo) {
        String url = fileStorageService.guardarImagen(archivo);
        return ResponseEntity.ok(productoService.agregarImagen(id, url));
    }

    @DeleteMapping("/{id}/imagenes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> eliminarImagen(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(productoService.eliminarImagen(id, body.get("url")));
    }

    @PatchMapping("/{id}/imagen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> actualizarImagen(
            @PathVariable Long id,
            @RequestBody ImagenRequest request) {
        return ResponseEntity.ok(productoService.actualizarImagenUrl(id, request.getImagenUrl()));
    }

    @PostMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addStock(@PathVariable Long id, @RequestBody StockRequest request) {
        productoService.addStockToProducto(id, request.getTalleId(), request.getCantidad());
        return ResponseEntity.ok().build();
    }
}
