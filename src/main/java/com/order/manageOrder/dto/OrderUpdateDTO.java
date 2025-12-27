package com.order.manageOrder.dto;

public class OrderUpdateDTO {

    private Long shippingContactMechId;
    private Long billingContactMechId;

    // Constructors
    public OrderUpdateDTO() {
    }

    public OrderUpdateDTO(Long shippingContactMechId, Long billingContactMechId) {
        this.shippingContactMechId = shippingContactMechId;
        this.billingContactMechId = billingContactMechId;
    }

    // Getters and Setters
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
}
