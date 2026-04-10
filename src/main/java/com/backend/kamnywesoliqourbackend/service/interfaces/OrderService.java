package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.dto.req.PlaceOrderReq;
import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(UUID id);
    List<Order> getBranchOrders(UUID branchId);
    Order createOrder(PlaceOrderReq req);
    Order updateOrderStatus(UUID id , OrderStatus status);
//    void deleteOrder(UUID id);
}
