package com.backend.kamnywesoliqourbackend.service.impl;

import com.backend.kamnywesoliqourbackend.entity.Drink;
import com.backend.kamnywesoliqourbackend.repository.DrinkRepository;
import com.backend.kamnywesoliqourbackend.service.interfaces.DrinkService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DrinkServiceImpl implements DrinkService {
    private final DrinkRepository drinkRepository;

    public DrinkServiceImpl(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public List<Drink> getAllDrinks() {
        return drinkRepository.findAll();
    }

    public Drink getDrinkById(UUID id) {
        return drinkRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Drink not found with id: " + id));
    }

    public Drink createDrink(Drink drink) {
        return drinkRepository.save(drink);
    }

    public void deleteDrink(UUID id) {
        drinkRepository.deleteById(id);
    }
}
