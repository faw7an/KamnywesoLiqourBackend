package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.dto.req.OrderItemReq;
import com.backend.kamnywesoliqourbackend.dto.req.PlaceOrderReq;
import com.backend.kamnywesoliqourbackend.entity.*;
import com.backend.kamnywesoliqourbackend.enums.OrderStatus;
import com.backend.kamnywesoliqourbackend.enums.TransactionType;
import com.backend.kamnywesoliqourbackend.repository.*;
import com.backend.kamnywesoliqourbackend.service.interfaces.LoyaltyService;
import com.backend.kamnywesoliqourbackend.service.interfaces.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BranchRepository branchRepository;
    private final DrinkRepository drinkRepository;
    private final StockRepository stockRepository;
    private final LoyaltyService loyaltyService;
    private UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, BranchRepository branchRepository, UserRepository userRepository, DrinkRepository drinkRepository, StockRepository stockRepository, LoyaltyService loyaltyService){
        this.orderRepository = orderRepository;
        this.branchRepository = branchRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.loyaltyService = loyaltyService;
        this.drinkRepository = drinkRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(UUID id) {
        return orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getBranchOrders(UUID branchId) {
        return orderRepository.findByBranch_Id(branchId);
    }

    @Override
    @Transactional
    public Order createOrder(PlaceOrderReq req) {
        Branch branch = branchRepository.findById(req.branchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        User staff = userRepository.findById(req.staffId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));

        Order order = new Order();
        order.setStaff(staff);
        order.setBranch(branch);
        order.setCustomerName(req.customerName());
        order.setStatus(OrderStatus.COMPLETED);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemReq item : req.items()) {
            Drink drink = drinkRepository.findById(item.drinkId()).orElseThrow(() -> new RuntimeException("Drink not found"));
            Stock stock = stockRepository.findByDrink_IdAndBranch_Id(item.drinkId(), branch.getId()).orElseThrow(() -> new RuntimeException("Not stock record found for drink: " + drink.getName()));

            if (stock.getQuantity() < item.quantity()) {
                throw new RuntimeException("Not enough stock for drink: " + drink.getName());
            }

            stock.setQuantity(stock.getQuantity() - item.quantity());
            stockRepository.save(stock);

            OrderItem orderItem = new OrderItem();

            orderItem.setDrink(drink);
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(order);
            orderItem.setUnitPrice(drink.getPrice());

            BigDecimal subTotal = drink.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
            totalAmount = totalAmount.add(subTotal);

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        if (req.loyaltyCardId() != null) {
            // 1 point for every 100 units of currency
            int pointsToEarn = order.getTotalAmount().divide(BigDecimal.valueOf(100)).intValue();

            loyaltyService.createLoyaltyTransaction(
                    req.loyaltyCardId(),
                    savedOrder.getId(),
                    TransactionType.EARNED,
                    pointsToEarn
            );
        }
        return savedOrder;
    }
    @Override
    public Order updateOrderStatus(UUID id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }
}
