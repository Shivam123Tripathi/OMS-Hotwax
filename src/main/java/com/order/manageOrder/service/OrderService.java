package com.order.manageOrder.service;

import com.order.manageOrder.dto.OrderItemDTO;
import com.order.manageOrder.dto.OrderRequestDTO;
import com.order.manageOrder.dto.OrderResponseDTO;
import com.order.manageOrder.dto.OrderUpdateDTO;
import com.order.manageOrder.entity.*;
import com.order.manageOrder.exception.ResourceNotFoundException;
import com.order.manageOrder.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderHeaderRepository orderHeaderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ContactMechRepository contactMechRepository;
    private final ProductRepository productRepository;

    // Constructor injection (better practice than field injection)
    public OrderService(OrderHeaderRepository orderHeaderRepository,
                       OrderItemRepository orderItemRepository,
                       CustomerRepository customerRepository,
                       ContactMechRepository contactMechRepository,
                       ProductRepository productRepository) {
        this.orderHeaderRepository = orderHeaderRepository;
        this.orderItemRepository = orderItemRepository;
        this.customerRepository = customerRepository;
        this.contactMechRepository = contactMechRepository;
        this.productRepository = productRepository;
    }

    /**
     * Create a new order with multiple order items
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        // Fetch customer
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "customerId", orderRequest.getCustomerId()));

        // Create order header
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderDate(LocalDateTime.now());
        orderHeader.setCustomer(customer);

        // Set shipping contact if provided
        if (orderRequest.getShippingContactMechId() != null) {
            ContactMech shippingContact = contactMechRepository.findById(orderRequest.getShippingContactMechId())
                    .orElseThrow(() -> new ResourceNotFoundException("ContactMech", "contactMechId", orderRequest.getShippingContactMechId()));
            orderHeader.setShippingContactMech(shippingContact);
        }

        // Set billing contact if provided
        if (orderRequest.getBillingContactMechId() != null) {
            ContactMech billingContact = contactMechRepository.findById(orderRequest.getBillingContactMechId())
                    .orElseThrow(() -> new ResourceNotFoundException("ContactMech", "contactMechId", orderRequest.getBillingContactMechId()));
            orderHeader.setBillingContactMech(billingContact);
        }

        // Create order items
        for (OrderItemDTO itemDTO : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", itemDTO.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setStatus(itemDTO.getStatus() != null ? itemDTO.getStatus() : "PENDING");
            orderHeader.addOrderItem(orderItem);
        }

        // Save order
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);

        return convertToResponseDTO(savedOrder);
    }

    /**
     * Get order details by ID
     */
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        return convertToResponseDTO(orderHeader);
    }

    /**
     * Update order shipping or billing contact
     */
    @Transactional
    public OrderResponseDTO updateOrder(Long orderId, OrderUpdateDTO orderUpdate) {
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        // Update shipping contact if provided
        if (orderUpdate.getShippingContactMechId() != null) {
            ContactMech shippingContact = contactMechRepository.findById(orderUpdate.getShippingContactMechId())
                    .orElseThrow(() -> new ResourceNotFoundException("ContactMech", "contactMechId", orderUpdate.getShippingContactMechId()));
            orderHeader.setShippingContactMech(shippingContact);
        }

        // Update billing contact if provided
        if (orderUpdate.getBillingContactMechId() != null) {
            ContactMech billingContact = contactMechRepository.findById(orderUpdate.getBillingContactMechId())
                    .orElseThrow(() -> new ResourceNotFoundException("ContactMech", "contactMechId", orderUpdate.getBillingContactMechId()));
            orderHeader.setBillingContactMech(billingContact);
        }

        OrderHeader updatedOrder = orderHeaderRepository.save(orderHeader);
        return convertToResponseDTO(updatedOrder);
    }

    /**
     * Delete order by ID
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        orderHeaderRepository.delete(orderHeader);
    }

    /**
     * Add an order item to an existing order
     */
    @Transactional
    public OrderItemDTO addOrderItem(Long orderId, OrderItemDTO itemDTO) {
        OrderHeader orderHeader = orderHeaderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId));

        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", itemDTO.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setStatus(itemDTO.getStatus() != null ? itemDTO.getStatus() : "PENDING");
        orderHeader.addOrderItem(orderItem);

        orderHeaderRepository.save(orderHeader);

        return convertToOrderItemDTO(orderItem);
    }

    /**
     * Update an existing order item
     */
    @Transactional
    public OrderItemDTO updateOrderItem(Long orderId, Long orderItemSeqId, OrderItemDTO itemDTO) {
        OrderItem orderItem = orderItemRepository.findByOrderOrderIdAndOrderItemSeqId(orderId, orderItemSeqId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found in order " + orderId + " with itemSeqId: " + orderItemSeqId));

        // Update quantity and status
        if (itemDTO.getQuantity() != null) {
            orderItem.setQuantity(itemDTO.getQuantity());
        }
        if (itemDTO.getStatus() != null) {
            orderItem.setStatus(itemDTO.getStatus());
        }

        // Update product if provided
        if (itemDTO.getProductId() != null && !itemDTO.getProductId().equals(orderItem.getProduct().getProductId())) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", itemDTO.getProductId()));
            orderItem.setProduct(product);
        }

        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return convertToOrderItemDTO(updatedItem);
    }

    /**
     * Delete an order item
     */
    @Transactional
    public void deleteOrderItem(Long orderId, Long orderItemSeqId) {
        OrderItem orderItem = orderItemRepository.findByOrderOrderIdAndOrderItemSeqId(orderId, orderItemSeqId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found in order " + orderId + " with itemSeqId: " + orderItemSeqId));

        OrderHeader orderHeader = orderItem.getOrder();
        orderHeader.removeOrderItem(orderItem);
        orderItemRepository.delete(orderItem);
    }

    /**
     * Convert OrderHeader entity to OrderResponseDTO
     */
    private OrderResponseDTO convertToResponseDTO(OrderHeader orderHeader) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(orderHeader.getOrderId());
        responseDTO.setOrderDate(orderHeader.getOrderDate());

        // Set customer details
        Customer customer = orderHeader.getCustomer();
        responseDTO.setCustomerId(customer.getCustomerId());
        responseDTO.setCustomerFirstName(customer.getFirstName());
        responseDTO.setCustomerLastName(customer.getLastName());

        // Set shipping contact details
        if (orderHeader.getShippingContactMech() != null) {
            ContactMech shipping = orderHeader.getShippingContactMech();
            responseDTO.setShippingContactMechId(shipping.getContactMechId());
            responseDTO.setShippingStreetAddress(shipping.getStreetAddress());
            responseDTO.setShippingCity(shipping.getCity());
            responseDTO.setShippingState(shipping.getState());
            responseDTO.setShippingPostalCode(shipping.getPostalCode());
            responseDTO.setShippingPhoneNumber(shipping.getPhoneNumber());
            responseDTO.setShippingEmail(shipping.getEmail());
        }

        // Set billing contact details
        if (orderHeader.getBillingContactMech() != null) {
            ContactMech billing = orderHeader.getBillingContactMech();
            responseDTO.setBillingContactMechId(billing.getContactMechId());
            responseDTO.setBillingStreetAddress(billing.getStreetAddress());
            responseDTO.setBillingCity(billing.getCity());
            responseDTO.setBillingState(billing.getState());
            responseDTO.setBillingPostalCode(billing.getPostalCode());
            responseDTO.setBillingPhoneNumber(billing.getPhoneNumber());
            responseDTO.setBillingEmail(billing.getEmail());
        }

        // Set order items
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        for (OrderItem item : orderHeader.getOrderItems()) {
            itemDTOs.add(convertToOrderItemDTO(item));
        }
        responseDTO.setOrderItems(itemDTOs);

        return responseDTO;
    }

    /**
     * Convert OrderItem entity to OrderItemDTO
     */
    private OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setOrderItemSeqId(orderItem.getOrderItemSeqId());
        itemDTO.setProductId(orderItem.getProduct().getProductId());
        itemDTO.setQuantity(orderItem.getQuantity());
        itemDTO.setStatus(orderItem.getStatus());

        // Include product details
        Product product = orderItem.getProduct();
        itemDTO.setProductName(product.getProductName());
        itemDTO.setColor(product.getColor());
        itemDTO.setSize(product.getSize());

        return itemDTO;
    }
}
