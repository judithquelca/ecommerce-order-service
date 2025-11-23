package dev.judyquelca.orderservice.service;

import dev.judyquelca.orderservice.dto.OrderRequest;
import dev.judyquelca.orderservice.dto.OrderResponse;
import dev.judyquelca.orderservice.exception.OrderNotFoundException;
import dev.judyquelca.orderservice.kafka.consumer.OrderEventConsumer;
import dev.judyquelca.orderservice.kafka.event.OrderPlacedEvent;
import dev.judyquelca.orderservice.kafka.producer.OrderEventProducer;
import dev.judyquelca.orderservice.mapper.OrderMapper;
import dev.judyquelca.orderservice.model.Order;
import dev.judyquelca.orderservice.model.OrderStatus;
import dev.judyquelca.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer; // Nuevo

    public OrderService(OrderRepository orderRepository,
                        OrderEventProducer orderEventProducer) { // Nuevo
        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer; // Nuevo
    }

    @Transactional
    public OrderResponse create(OrderRequest request) {
        // 1. Crear y guardar orden
        Order order = new Order();
        OrderMapper.updateEntity(request, order);
        Order saved = orderRepository.save(order);

        // 2. Publicar evento a Kafka
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getId(),
                saved.getProductId(),
                saved.getQuantity(),
                saved.getCustomerName(),
                saved.getCustomerEmail(),
                saved.getTotalAmount()
        );
        orderEventProducer.publishOrderPlaced(event);

        // 3. Retornar respuesta
        return OrderMapper.toResponse(saved);
    }

    // Resto de métodos sin cambios...
    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                //.map(this::mapToResponse)
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Orden " + id + " no encontrado"));
        return OrderMapper.toResponse(order);
    }

    /**
     * Confirma una orden (actualiza estado de PENDING a CONFIRMED) Llamado cuando inventory-service
     * confirma que hay stock
     */
    @Transactional
    public void confirmOrder(Long orderId) {
        log.info("Confirming order: orderId={}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order no encontrado: " + orderId));

        // Verificar que la orden está en estado PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Order is not PENDING, cannot confirm: orderId={}, currentStatus={}",
                    orderId, order.getStatus());
            return; // Idempotencia: Si ya fue procesada, no hacer nada
        }

        // Actualizar estado
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        log.info("Order confirmed: orderId={}, newStatus={}", orderId, order.getStatus());
    }

    /**
     * Cancela una orden (actualiza estado de PENDING a CANCELLED) Llamado cuando inventory-service
     * rechaza la orden por falta de stock
     */
    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        log.info("Cancelling order: orderId={}, reason={}", orderId, reason);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order no encontrada: " + orderId));

        // Verificar que la orden está en estado PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Order is not PENDING, cannot cancel: orderId={}, currentStatus={}",
                    orderId, order.getStatus());
            return; // Idempotencia: Si ya fue procesada, no hacer nada
        }

        // Actualizar estado y guardar razón de cancelación
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        orderRepository.save(order);

        log.info("Order cancelled: orderId={}, newStatus={}, reason={}",
                orderId, order.getStatus(), reason);
    }

}