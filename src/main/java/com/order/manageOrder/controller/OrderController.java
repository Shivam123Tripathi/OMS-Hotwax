package com.order.manageOrder.controller;

import com.order.manageOrder.dto.OrderItemDTO;
import com.order.manageOrder.dto.OrderRequestDTO;
import com.order.manageOrder.dto.OrderResponseDTO;
import com.order.manageOrder.dto.OrderUpdateDTO;
import com.order.manageOrder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // Constructor injection (better practice than field injection)
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * creating a new order with multiple order items
     * POST / --orders for posting orders
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Get order details by ID
     * GET /orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * UpdaTe order shipping or billing contact
     * PUT /orders/{orderId}
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateDTO orderUpdate) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(orderId, orderUpdate);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Delete order by ID
     * DELETE /orders/{orderId}
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add an order item to an existing order
     * POST /orders/{orderId}/items
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItemDTO> addOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemDTO itemDTO) {
        OrderItemDTO createdItem = orderService.addOrderItem(orderId, itemDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Update an existing order item
     * PUT /orders/{orderId}/items/{orderItemSeqId}
     */
    @PutMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long orderItemSeqId,
            @RequestBody OrderItemDTO itemDTO) {
        OrderItemDTO updatedItem = orderService.updateOrderItem(orderId, orderItemSeqId, itemDTO);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Delete an order item
     * DELETE /orders/{orderId}/items/{orderItemSeqId}
     */
    @DeleteMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long orderItemSeqId) {
        orderService.deleteOrderItem(orderId, orderItemSeqId);
        return ResponseEntity.noContent().build();
    }
}
