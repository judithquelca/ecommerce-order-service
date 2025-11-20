package dev.judyquelca.orderservice.mapper;

import dev.judyquelca.orderservice.dto.OrderRequest;
import dev.judyquelca.orderservice.dto.OrderResponse;
import dev.judyquelca.orderservice.model.entity.Order;


public final class OrderMapper {

    private OrderMapper() {
        throw new AssertionError("Utility class, no debe instanciarse");
    }

    public static OrderResponse toResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()

        );
    }

    public static void updateEntity(OrderRequest request, Order entity) {
        entity.setProductId(request.productId());
        entity.setQuantity(request.quantity());
        entity.setCustomerName(request.customerName());
        entity.setCustomerEmail(request.customerEmail());
        entity.setTotalAmount(request.totalAmount());

    }
}
