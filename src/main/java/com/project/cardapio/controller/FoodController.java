package com.project.cardapio.controller;

import com.project.cardapio.entities.Food;
import com.project.cardapio.repositories.FoodRepository;
import com.project.cardapio.entities.FoodRequestDTO;
import com.project.cardapio.entities.FoodResponseDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("food")
public class FoodController {
    @Autowired
    private FoodRepository repository;

    @GetMapping
    public List<FoodResponseDTO> getAll(){
        // Para cada 'Food' cria-se um novo 'FoodResponseDTO'
        return repository.findAll().stream().map(FoodResponseDTO::new).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Limitando acesso
    @CrossOrigin(origins = "http://localhost:8080")
    public void saveFood(@RequestBody FoodRequestDTO data){
        Food foodData = new Food(data);
        repository.save(foodData);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FoodRequestDTO> updateFood(@PathVariable Long id, @RequestBody FoodRequestDTO data){
        Optional<Food> optionalFood = repository.findById(id);

        if(optionalFood.isPresent()){
            Food existingFood = optionalFood.get();
            // Copia os dados de "data" para "existingFood"
            // Para a cópia funcionar, é necessário os métodos Setters na classe entidade.
            BeanUtils.copyProperties(data, existingFood);

            Food foodUpdated = repository.save(existingFood);

            return new ResponseEntity<FoodRequestDTO>(new FoodRequestDTO(
                        foodUpdated.getTitle(),
                        foodUpdated.getImage(),
                        foodUpdated.getPrice()),
                        HttpStatus.OK);
        }
        // Retorna 404 Not Found se o objeto não for encontrado
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFood(@PathVariable Long id){
        Food existingFood = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Jogo não encontrado"));
        repository.deleteById(id);
    }

}
