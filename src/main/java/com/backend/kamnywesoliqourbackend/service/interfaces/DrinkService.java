package com.backend.kamnywesoliqourbackend.service.interfaces;

import com.backend.kamnywesoliqourbackend.entity.Drink;

import java.util.List;
import java.util.UUID;

public interface DrinkService {
    List<Drink> getAllDrinks();

    Drink getDrinkById(UUID id);
    Drink createDrink(Drink drink);
    void deleteDrink(UUID id);
}
