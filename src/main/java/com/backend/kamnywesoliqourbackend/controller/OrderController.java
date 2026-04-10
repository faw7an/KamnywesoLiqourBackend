package com.backend.kamnywesoliqourbackend.controller;

import com.backend.kamnywesoliqourbackend.dto.req.PlaceOrderReq;
import com.backend.kamnywesoliqourbackend.dto.res.OrderItemRes;
import com.backend.kamnywesoliqourbackend.dto.res.OrderRes;
import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.OrderItem;
import com.backend.kamnywesoliqourbackend.service.interfaces.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderRes>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(
                orders.stream().map(this::mapToOrderRes).toList()
        );
    }

    @GetMapping("/{branchid}")
    public ResponseEntity<List<OrderRes>> getBranchOrders(@PathVariable UUID branchid) {
        List<Order> order = orderService.getBranchOrders(branchid);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order.stream().map(this::mapToOrderRes).toList());
    }

    @PostMapping
    public ResponseEntity<OrderRes> placeOrder(@RequestBody PlaceOrderReq req) {
        Order order = orderService.createOrder(req);
        return ResponseEntity.ok(mapToOrderRes(order));
    }

    private OrderRes mapToOrderRes(Order order) {
        List<OrderItemRes> itemDtos = order.getOrderItems().stream().map(this::mapToOrderItemRes).toList();
        return  new OrderRes(
                order.getId(),
                order.getCustomerName(),
                order.getCustomerPhone(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getBranch() != null ? order.getBranch().getName() : "Unknown Branch",
                itemDtos
                );
    }
    private OrderItemRes mapToOrderItemRes(OrderItem item) {
        return new OrderItemRes(
                item.getDrink().getName(),
                item.getQuantity(),
                 item.getUnitPrice(),
                // subTotal = price * quantity
                item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
        );
    }
}
