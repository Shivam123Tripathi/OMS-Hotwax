package com.order.manageOrder.dto;

import jakarta.validation.constraints.NotNull;

public class OrderItemDTO {

    private Long orderItemSeqId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String status;

    // Product details for response
    private String productName;
    private String color;
    private String size;

    // Constructors
    public OrderItemDTO() {
    }

    public OrderItemDTO(Long productId, Integer quantity, String status) {
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderItemSeqId() {
        return orderItemSeqId;
    }

    public void setOrderItemSeqId(Long orderItemSeqId) {
        this.orderItemSeqId = orderItemSeqId;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
