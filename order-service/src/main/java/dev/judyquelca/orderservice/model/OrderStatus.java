package dev.judyquelca.orderservice.model;

public enum OrderStatus {
  PENDING,      // Orden creada, esperando validación
  CONFIRMED,    // Orden confirmada por inventory-service
  CANCELLED     // Orden cancelada (sin stock)
}
