package com.uade.tpo.demo.controllers.pedidos;

import com.uade.tpo.demo.entity.Pedido;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidosController {
    private final PedidoService pedidoService;
    
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody List<PedidoItemRequest> items) {
        
        User user = (User) userDetails;
        Pedido pedido = pedidoService.crearPedido(user, items);
        return ResponseEntity.ok(pedido);
    }
    
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> getMisPedidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return ResponseEntity.ok(pedidoService.getPedidosByUser(user.getId()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedidoById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Pedido pedido = pedidoService.getPedidoById(id);
        
        // Verificar que el pedido pertenezca al usuario o sea admin
        User user = (User) userDetails;
        if (!pedido.getUsuario().getId().equals(user.getId()) && 
            !user.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(pedido);
    }
    
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id, 
            @RequestBody EstadoRequest request) {
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, request.getEstado()));
    }
}