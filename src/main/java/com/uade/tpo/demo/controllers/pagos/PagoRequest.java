package com.uade.tpo.demo.controllers.pagos;

import lombok.Data;

@Data
public class PagoRequest {
    private Long pedidoId;
    private String metodo; // TARJETA_CREDITO, TARJETA_DEBITO, MERCADO_PAGO, EFECTIVO
}
