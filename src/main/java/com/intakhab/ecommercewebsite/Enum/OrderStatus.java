package com.intakhab.ecommercewebsite.Enum;

public enum OrderStatus {
    PENDING,         // Order has been placed but not processed yet
    PROCESSING,      // Order is being processed
    SHIPPED,         // Order has been shipped
    OUT_FOR_DELIVERY,// Order is out for delivery
    DELIVERED,       // Order has been delivered to the customer
    CANCELLED,       // Order has been cancelled by the customer or the system
    RETURNED         // Order has been returned by the customer
}
