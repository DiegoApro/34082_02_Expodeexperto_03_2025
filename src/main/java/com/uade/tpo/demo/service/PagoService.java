package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.Pago;
import com.uade.tpo.demo.entity.Pedido;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.PagoRepository;
import com.uade.tpo.demo.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public Pago procesarPago(User usuario, Long pedidoId, String metodo) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Verificar que el pedido pertenezca al usuario
        if (!pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tenés permiso para pagar este pedido");
        }

        // Verificar que el pedido esté en estado PENDIENTE
        if (!pedido.getEstado().equals("PENDIENTE")) {
            throw new RuntimeException("El pedido no está en estado PENDIENTE. Estado actual: " + pedido.getEstado());
        }

        // Verificar que no tenga ya un pago
        if (pagoRepository.findByPedidoId(pedidoId).isPresent()) {
            throw new RuntimeException("Este pedido ya tiene un pago registrado");
        }

        // Crear el pago
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodo(metodo);
        pago.setMonto(pedido.getTotal());
        pago.setFecha(LocalDateTime.now());
        pago.setEstado("COMPLETADO");

        Pago savedPago = pagoRepository.save(pago);

        // Actualizar el estado del pedido a PAGADO
        pedido.setEstado("PAGADO");
        pedidoRepository.save(pedido);

        return savedPago;
    }

    public Pago getPagoByPedidoId(Long pedidoId) {
        return pagoRepository.findByPedidoId(pedidoId)
            .orElseThrow(() -> new RuntimeException("No se encontró pago para el pedido " + pedidoId));
    }

    public Pago getPagoById(Long pagoId) {
        return pagoRepository.findById(pagoId)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }
}
