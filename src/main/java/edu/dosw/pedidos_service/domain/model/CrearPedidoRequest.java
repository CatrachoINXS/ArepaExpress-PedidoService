package edu.dosw.pedidos_service.domain.model;

import java.util.List;

public record CrearPedidoRequest(String cliente, List<String> arepas) {}
