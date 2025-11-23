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


/*public class OrderRequest {

  @NotNull(message = "El ID del producto es requerido")
  @Positive(message = "El ID del producto debe ser positivo")
  private Long productId;

  @NotNull(message = "La cantidad es requerida")
  @Positive(message = "La cantidad debe ser positiva")
  @Max(value = 100, message = "La cantidad no puede exceder 100")
  private Integer quantity;

  @NotBlank(message = "El nombre del cliente es requerido")
  @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
  private String customerName;

  @NotBlank(message = "El email del cliente es requerido")
  @Email(message = "El email debe ser válido")
  private String customerEmail;

  @NotNull(message = "El monto total es requerido")
  @Positive(message = "El monto total debe ser positivo")
  private BigDecimal totalAmount;

  // Getters y Setters
  public Long getProductId() {
    return productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
}*/
