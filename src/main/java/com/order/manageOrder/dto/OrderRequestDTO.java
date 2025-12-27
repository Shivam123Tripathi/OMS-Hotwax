package com.order.manageOrder.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private Long shippingContactMechId;
    private Long billingContactMechId;

    @NotNull(message = "Order items are required")
    private List<OrderItemDTO> orderItems;

    // Constructors
    public OrderRequestDTO() {
    }

    public OrderRequestDTO(Long customerId, List<OrderItemDTO> orderItems) {
        this.customerId = customerId;
        this.orderItems = orderItems;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getShippingContactMechId() {
        return shippingContactMechId;
    }

    public void setShippingContactMechId(Long shippingContactMechId) {
        this.shippingContactMechId = shippingContactMechId;
    }

    public Long getBillingContactMechId() {
        return billingContactMechId;
    }

    public void setBillingContactMechId(Long billingContactMechId) {
        this.billingContactMechId = billingContactMechId;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
