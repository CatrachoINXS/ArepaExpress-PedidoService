package edu.dosw.pedidos_service.application.usecase;

import edu.dosw.pedidos_service.domain.model.CrearPedidoRequest;
import edu.dosw.pedidos_service.domain.model.PedidoCreadoEvent;
import edu.dosw.pedidos_service.domain.port.in.CrearPedidoUseCase;
import edu.dosw.pedidos_service.domain.port.out.PedidoEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CrearPedidoUseCaseImpl implements CrearPedidoUseCase {

    private final PedidoEventPublisher publisher;

    public CrearPedidoUseCaseImpl(PedidoEventPublisher publisher) { this.publisher = publisher; }

    @Override
    public String crear(CrearPedidoRequest pedido) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        publisher.publicarPedidoCreado(new PedidoCreadoEvent(
            id, pedido.cliente(), pedido.arepas(), LocalDateTime.now().toString()));
        return id;
    }
}