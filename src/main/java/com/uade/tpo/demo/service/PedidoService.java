package com.uade.tpo.demo.service;

import com.uade.tpo.demo.controllers.pedidos.PedidoItemRequest;
import com.uade.tpo.demo.entity.*;
import com.uade.tpo.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final ProductoTalleRepository productoTalleRepository;
    
    @Transactional
    public Pedido crearPedido(User usuario, List<PedidoItemRequest> items) {
        // Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        
        Pedido savedPedido = pedidoRepository.save(pedido);
        
        // Procesar cada item del pedido
        for (PedidoItemRequest item : items) {
            ProductoTalle pt = productoTalleRepository.findById(item.getProductoTalleId())
                .orElseThrow(() -> new RuntimeException("Producto con talle no encontrado: " + item.getProductoTalleId()));
            
            // Verificar stock
            if (pt.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + pt.getProducto().getNombre() + 
                                         " talle " + pt.getTalle().getNombre() + 
                                         ". Disponible: " + pt.getStock());
            }
            
            // Actualizar stock
            pt.setStock(pt.getStock() - item.getCantidad());
            productoTalleRepository.save(pt);
            
            // Crear detalle del pedido
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(savedPedido);
            detalle.setProductoTalle(pt);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(pt.getProducto().getPrecio());
            
            detallePedidoRepository.save(detalle);
        }
        
        return pedidoRepository.findById(savedPedido.getId()).orElseThrow();
    }
    
    public List<Pedido> getPedidosByUser(Long userId) {
        return pedidoRepository.findByUsuarioId(userId);
    }
    
    public Pedido getPedidoById(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
    
    @Transactional
    public Pedido actualizarEstadoPedido(Long pedidoId, String estado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }
}