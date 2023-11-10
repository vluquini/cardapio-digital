package com.project.cardapio.controller;

import com.project.cardapio.entities.Food;
import com.project.cardapio.entities.FoodRepository;
import com.project.cardapio.entities.FoodRequestDTO;
import com.project.cardapio.entities.FoodResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    private FoodRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Limitando acesso
    @CrossOrigin(origins = "http://localhost:8080")
    public void saveFood(@RequestBody FoodRequestDTO data){
        Food foodData = new Food(data);
        repository.save(foodData);
    }
    @GetMapping
    public List<FoodResponseDTO> getAll(){
        // Para cada 'Food' cria-se um novo 'FoodResponseDTO'
        return repository.findAll().stream().map(FoodResponseDTO::new).toList();
    }
}
