package com.uade.tpo.demo.controllers.pagos;

import com.uade.tpo.demo.entity.Pago;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagosController {

    private final PagoService pagoService;

    // POST /api/pagos — procesar pago de un pedido
    @PostMapping
    public ResponseEntity<Pago> procesarPago(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PagoRequest request) {
        User user = (User) userDetails;
        Pago pago = pagoService.procesarPago(user, request.getPedidoId(), request.getMetodo());
        return ResponseEntity.ok(pago);
    }

    // GET /api/pagos/{id} — obtener pago por id
    @GetMapping("/{id}")
    public ResponseEntity<Pago> getPagoById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        Pago pago = pagoService.getPagoById(id);

        // Solo el dueño del pedido o un admin puede ver el pago
        User user = (User) userDetails;
        if (!pago.getPedido().getUsuario().getId().equals(user.getId())
                && !user.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(pago);
    }

    // GET /api/pagos/pedido/{pedidoId} — obtener pago por pedido
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Pago> getPagoByPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long pedidoId) {
        Pago pago = pagoService.getPagoByPedidoId(pedidoId);

        User user = (User) userDetails;
        if (!pago.getPedido().getUsuario().getId().equals(user.getId())
                && !user.getRole().name().equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(pago);
    }
}
