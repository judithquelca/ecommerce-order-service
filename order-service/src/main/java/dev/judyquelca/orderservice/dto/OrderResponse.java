package dev.judyquelca.orderservice.dto;

import dev.judyquelca.orderservice.model.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(

        Long id,
        Long productId,
        Integer quantity,
        String customerName,
        String customerEmail,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
