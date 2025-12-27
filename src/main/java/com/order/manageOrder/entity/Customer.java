package com.order.manageOrder.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<ContactMech> contactMechs;

    @OneToMany(mappedBy = "customer")
    private List<OrderHeader> orders;

    // Constructors
    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ContactMech> getContactMechs() {
        return contactMechs;
    }

    public void setContactMechs(List<ContactMech> contactMechs) {
        this.contactMechs = contactMechs;
    }

    public List<OrderHeader> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderHeader> orders) {
        this.orders = orders;
    }


}
