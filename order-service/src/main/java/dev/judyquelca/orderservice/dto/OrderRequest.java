package dev.judyquelca.orderservice.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;


public record OrderRequest(

        @NotNull(message = "{order.product.notblank}")
        @Positive(message = "{order.product.positive}")
        Long productId,

        @NotNull(message = "{order.quantity.notblank}")
        @Positive(message = "{order.quantity.positive}")
        @Max(value = 100, message = "{order.quantity.max}")
        Integer quantity,

        @NotBlank(message = "{order.name.notblank}")
        @Size(min = 3, max = 100, message = "{order.name.min}")
        String customerName,

        @NotBlank(message = "{order.email.notblank}")
        @Email(message = "{order.email.email}")
        String customerEmail,

        @NotNull(message = "{order.totalAmount.notblank}")
        @Positive(message = "{order.totalAmount.positive}")
        BigDecimal totalAmount

) {
}
