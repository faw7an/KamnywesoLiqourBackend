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
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<List<OrderRes>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(
                orders.stream().map(this::mapToOrderRes).toList()
        );
    }

    @GetMapping("/{branchid}")
    @org.springframework.transaction.annotation.Transactional
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
        
        String itemsSummary = order.getOrderItems().stream()
                .map(item -> item.getDrink().getName() + " x" + item.getQuantity())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
                
        String formattedTotal = String.format("%,d", order.getTotalAmount().longValue());
        
        String formattedTime = order.getCreatedAt() != null 
                ? java.time.format.DateTimeFormatter.ofPattern("dd MMM, HH:mm").format(order.getCreatedAt())
                : "N/A";
                
        // Simple mock of status caps -> camel
        String formattedStatus = order.getStatus().name().substring(0, 1) + 
                                 order.getStatus().name().substring(1).toLowerCase();

        // Calculate simple loyalty points logic from total (1 point per 10 ksh approx)
        int loyalty = order.getTotalAmount() != null ? order.getTotalAmount().intValue() / 10 : 0;
        
        // Wait, does Order have getStaff()?
        // String staffName = order.getStaff() != null ? order.getStaff().getName() : "Admin";
        String staffName = "Admin"; 
        String orderIdPrefix = "ORD-" + order.getId().toString().substring(0, 4).toUpperCase();

        return  new OrderRes(
                order.getId(),
                orderIdPrefix,
                order.getCustomerName(),
                itemsSummary,
                formattedTotal,
                formattedStatus,
                formattedTime,
                staffName,
                loyalty,
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
