package com.backend.kamnywesoliqourbackend;

import com.backend.kamnywesoliqourbackend.entity.Branch;
import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.entity.Stock;
import com.backend.kamnywesoliqourbackend.entity.User;
import com.backend.kamnywesoliqourbackend.entity.Order;
import com.backend.kamnywesoliqourbackend.entity.OrderItem;
import com.backend.kamnywesoliqourbackend.enums.Role;
import com.backend.kamnywesoliqourbackend.enums.UserStatus;
import com.backend.kamnywesoliqourbackend.enums.OrderStatus;
import com.backend.kamnywesoliqourbackend.repository.BranchRepository;
import com.backend.kamnywesoliqourbackend.repository.DrinkRepository;
import com.backend.kamnywesoliqourbackend.repository.StockRepository;
import com.backend.kamnywesoliqourbackend.repository.UserRepository;
import com.backend.kamnywesoliqourbackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DrinkRepository drinkRepository;
    private final BranchRepository branchRepository;
    private final StockRepository stockRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, DrinkRepository drinkRepository, BranchRepository branchRepository, StockRepository stockRepository, OrderRepository orderRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.drinkRepository = drinkRepository;
        this.branchRepository = branchRepository;
        this.stockRepository = stockRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@test.com");
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
        }

        if (branchRepository.count() <= 1) {
            // First check if Nakuru exists
            if (branchRepository.findAll().stream().noneMatch(b -> "Nakuru".equals(b.getName()))) {
                Branch nakuru = new Branch();
                nakuru.setName("Nakuru");
                nakuru.setLocation("Nakuru CBD");
                nakuru.setHq(false);
                branchRepository.save(nakuru);
            }

            if (branchRepository.findAll().stream().noneMatch(b -> "Mombasa".equals(b.getName()))) {
                Branch mombasa = new Branch();
                mombasa.setName("Mombasa");
                mombasa.setLocation("Mombasa Island");
                mombasa.setHq(false);
                branchRepository.save(mombasa);
            }
            
            if (branchRepository.findAll().stream().noneMatch(b -> "Kisumu".equals(b.getName()))) {
                Branch kisumu = new Branch();
                kisumu.setName("Kisumu");
                kisumu.setLocation("Kisumu City");
                kisumu.setHq(false);
                branchRepository.save(kisumu);
            }
        }

            if (drinkRepository.count() == 0) {
                Drink d1 = new Drink();
                d1.setName("Tusker Lager"); d1.setBrand("EABL"); d1.setPrice(new BigDecimal("250")); d1.setCostPrice(new BigDecimal("150")); d1.setCategory("BEER"); d1.setImage("/tusker.png");
                Drink d2 = new Drink();
                d2.setName("Guinness"); d2.setBrand("Diageo"); d2.setPrice(new BigDecimal("300")); d2.setCostPrice(new BigDecimal("200")); d2.setCategory("BEER"); d2.setImage("/guinness.png");
                Drink d3 = new Drink();
                d3.setName("Coca Cola"); d3.setBrand("Coke"); d3.setPrice(new BigDecimal("100")); d3.setCostPrice(new BigDecimal("50")); d3.setCategory("SODA"); d3.setImage("/coca-cola.png");
                Drink d4 = new Drink();
                d4.setName("White Cap"); d4.setBrand("EABL"); d4.setPrice(new BigDecimal("280")); d4.setCostPrice(new BigDecimal("180")); d4.setCategory("BEER"); d4.setImage("/white-cap.png");
                Drink d5 = new Drink();
                d5.setName("Johnnie Walker"); d5.setBrand("Diageo"); d5.setPrice(new BigDecimal("450")); d5.setCostPrice(new BigDecimal("350")); d5.setCategory("WHISKEY"); d5.setImage("/johnnie-walker.png");
                Drink d6 = new Drink();
                d6.setName("Fanta Orange"); d6.setBrand("Coke"); d6.setPrice(new BigDecimal("100")); d6.setCostPrice(new BigDecimal("50")); d6.setCategory("SODA"); d6.setImage("/fanta.png");
                Drink d7 = new Drink();
                d7.setName("Stoney Ginger"); d7.setBrand("Coke"); d7.setPrice(new BigDecimal("100")); d7.setCostPrice(new BigDecimal("50")); d7.setCategory("SODA"); d7.setImage("/stoney.png");
                Drink d8 = new Drink();
                d8.setName("Smirnoff Vodka"); d8.setBrand("Diageo"); d8.setPrice(new BigDecimal("350")); d8.setCostPrice(new BigDecimal("250")); d8.setCategory("VODKA"); d8.setImage("/smirnoff.png");
                Drink d9 = new Drink();
                d9.setName("Bell's Whisky"); d9.setBrand("Diageo"); d9.setPrice(new BigDecimal("380")); d9.setCostPrice(new BigDecimal("280")); d9.setCategory("WHISKEY"); d9.setImage("/bells.png");
                Drink d10 = new Drink();
                d10.setName("Tusker Cider"); d10.setBrand("EABL"); d10.setPrice(new BigDecimal("220")); d10.setCostPrice(new BigDecimal("120")); d10.setCategory("CIDER"); d10.setImage("/tusker-cider.png");
                
                drinkRepository.saveAll(List.of(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10));

                List<Drink> drinks = drinkRepository.findAll();
                Branch nakuru = branchRepository.findAll().stream().filter(b -> "Nakuru".equals(b.getName())).findFirst().orElse(null);
                Branch mombasa = branchRepository.findAll().stream().filter(b -> "Mombasa".equals(b.getName())).findFirst().orElse(null);

                if (nakuru != null && mombasa != null) {
                    Stock s1 = new Stock(); s1.setDrink(drinks.get(0)); s1.setBranch(nakuru); s1.setQuantity(48); s1.setMinThreshold(20); s1.setLastRestockedAt(LocalDateTime.now());
                    Stock s2 = new Stock(); s2.setDrink(drinks.get(1)); s2.setBranch(nakuru); s2.setQuantity(8); s2.setMinThreshold(10); s2.setLastRestockedAt(LocalDateTime.now());
                    Stock s3 = new Stock(); s3.setDrink(drinks.get(2)); s3.setBranch(mombasa); s3.setQuantity(120); s3.setMinThreshold(30); s3.setLastRestockedAt(LocalDateTime.now());
                    Stock s4 = new Stock(); s4.setDrink(drinks.get(3)); s4.setBranch(mombasa); s4.setQuantity(8); s4.setMinThreshold(15); s4.setLastRestockedAt(LocalDateTime.now());
                    Stock s5 = new Stock(); s5.setDrink(drinks.get(5)); s5.setBranch(nakuru); s5.setQuantity(0); s5.setMinThreshold(20); s5.setLastRestockedAt(LocalDateTime.now());
                    Stock s6 = new Stock(); s6.setDrink(drinks.get(6)); s6.setBranch(nakuru); s6.setQuantity(45); s6.setMinThreshold(20); s6.setLastRestockedAt(LocalDateTime.now());
                    Stock s7 = new Stock(); s7.setDrink(drinks.get(7)); s7.setBranch(nakuru); s7.setQuantity(15); s7.setMinThreshold(10); s7.setLastRestockedAt(LocalDateTime.now());

                    stockRepository.saveAll(List.of(s1, s2, s3, s4, s5, s6, s7));
                }
            }
        
        // Add Demo Orders
        if (orderRepository.count() == 0) {
            List<Drink> drinks = drinkRepository.findAll();
            List<Branch> branches = branchRepository.findAll();
            if(!drinks.isEmpty() && !branches.isEmpty()) {
                Branch nakuru = branches.stream().filter(b -> b.getName().equals("Nakuru")).findFirst().orElse(branches.get(0));
                Branch mombasa = branches.stream().filter(b -> b.getName().equals("Mombasa")).findFirst().orElse(branches.get(0));
                Branch kisumu = branches.stream().filter(b -> b.getName().equals("Kisumu")).findFirst().orElse(branches.get(0));
                Branch hq = branches.stream().filter(b -> b.getName().equals("HQ")).findFirst().orElse(branches.get(0));

                Order o1 = new Order();
                o1.setCustomerName("Harrison Mbugua");
                o1.setBranch(nakuru);
                o1.setStatus(OrderStatus.PROCESSING);
                o1.setTotalAmount(new BigDecimal("4800"));
                o1.setCreatedAt(LocalDateTime.now().minusHours(2));
                OrderItem item1 = new OrderItem(); item1.setOrder(o1); item1.setDrink(drinks.get(0)); item1.setQuantity(12); item1.setUnitPrice(new BigDecimal("250")); item1.setPointsEarned(0);
                OrderItem item2 = new OrderItem(); item2.setOrder(o1); item2.setDrink(drinks.get(1)); item2.setQuantity(4); item2.setUnitPrice(new BigDecimal("300")); item2.setPointsEarned(0);
                o1.setOrderItems(List.of(item1, item2));

                Order o2 = new Order();
                o2.setCustomerName("Grace Wanjiku");
                o2.setBranch(mombasa);
                o2.setStatus(OrderStatus.PENDING);
                o2.setTotalAmount(new BigDecimal("3200"));
                o2.setCreatedAt(LocalDateTime.now().minusHours(1));
                OrderItem item3 = new OrderItem(); item3.setOrder(o2); item3.setDrink(drinks.get(2)); item3.setQuantity(24); item3.setUnitPrice(new BigDecimal("100")); item3.setPointsEarned(0);
                o2.setOrderItems(List.of(item3));

                Order o3 = new Order();
                o3.setCustomerName("David Ochieng");
                o3.setBranch(kisumu);
                o3.setStatus(OrderStatus.CANCELLED);
                o3.setTotalAmount(new BigDecimal("1680"));
                o3.setCreatedAt(LocalDateTime.now().minusMinutes(30));
                OrderItem item4 = new OrderItem(); item4.setOrder(o3); item4.setDrink(drinks.get(3)); item4.setQuantity(6); item4.setUnitPrice(new BigDecimal("280")); item4.setPointsEarned(0);
                o3.setOrderItems(List.of(item4));

                Order o4 = new Order();
                o4.setCustomerName("Peter Kamau");
                o4.setBranch(nakuru);
                o4.setStatus(OrderStatus.COMPLETED);
                o4.setTotalAmount(new BigDecimal("6000"));
                o4.setCreatedAt(LocalDateTime.now().minusMinutes(15));
                OrderItem item5 = new OrderItem(); item5.setOrder(o4); item5.setDrink(drinks.get(0)); item5.setQuantity(24); item5.setUnitPrice(new BigDecimal("250")); item5.setPointsEarned(0);
                o4.setOrderItems(List.of(item5));
                
                Order o5 = new Order();
                o5.setCustomerName("Samuel Otieno");
                o5.setBranch(hq);
                o5.setStatus(OrderStatus.PENDING);
                o5.setTotalAmount(new BigDecimal("2400"));
                o5.setCreatedAt(LocalDateTime.now().minusMinutes(5));
                OrderItem item6 = new OrderItem(); item6.setOrder(o5); item6.setDrink(drinks.get(4)); item6.setQuantity(1); item6.setUnitPrice(new BigDecimal("2400")); item6.setPointsEarned(0);
                o5.setOrderItems(List.of(item6));
                
                orderRepository.saveAll(List.of(o1, o2, o3, o4, o5));
            }
        }
    }
}
