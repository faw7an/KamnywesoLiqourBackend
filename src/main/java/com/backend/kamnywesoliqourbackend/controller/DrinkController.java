package com.backend.kamnywesoliqourbackend.controller;


import com.backend.kamnywesoliqourbackend.dto.res.DrinkRes;
import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.service.interfaces.DrinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/drinks")
public class DrinkController {
    private final DrinkService drinkService;

    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping
    public ResponseEntity<List<DrinkRes>> getAllDrinks() {
        List<Drink> drinks = drinkService.getAllDrinks();
        return ResponseEntity.ok(
                drinks.stream().map(this::mapToDrinkRes).toList()
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DrinkRes> getDrinkById(@PathVariable UUID id) {
        Drink drink = drinkService.getDrinkById(id);
        return ResponseEntity.ok(mapToDrinkRes(drink));
    }

    @PostMapping
    public ResponseEntity<DrinkRes> createDrink(@RequestBody Drink drink) {
        Drink createdDrink = drinkService.createDrink(drink);
        return ResponseEntity.ok(mapToDrinkRes(createdDrink));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrink(@PathVariable UUID id) {
        drinkService.deleteDrink(id);
        return ResponseEntity.noContent().build();
    }


    private DrinkRes mapToDrinkRes(Drink drink) {
        return new DrinkRes(
                drink.getId(),
                drink.getName(),
                drink.getBrand(),
                drink.getCategory(),
                drink.getPrice(),
                drink.getImage()
        );
    }
}
