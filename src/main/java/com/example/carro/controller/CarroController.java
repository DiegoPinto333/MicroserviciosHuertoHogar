package com.example.carro.controller;

import com.example.carro.dto.CarroRequestDTO;
import com.example.carro.dto.CarroResponseDTO;
import com.example.carro.service.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    public ResponseEntity<List<CarroResponseDTO>> listarCarros() {
        List<CarroResponseDTO> carros = carroService.getAll();
        return ResponseEntity.ok(carros); // Siempre devuelve 200 OK, incluso si la lista está vacía.
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroResponseDTO> obtenerCarro(@PathVariable("id") int id) {
        CarroResponseDTO carro = carroService.getCarroById(id);
        if (carro == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carro);
    }

    @PostMapping
    public ResponseEntity<CarroResponseDTO> guardarCarro(@RequestBody CarroRequestDTO carroDTO) { // Cambiado a 201 Created
        CarroResponseDTO nuevoCarro = carroService.save(carroDTO);
        return new ResponseEntity<>(nuevoCarro, HttpStatus.CREATED);
    }

    // Nuevo endpoint para buscar carros por userId
    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<CarroResponseDTO>> listarCarrosPorUsuario(@PathVariable("userId") int userId) {
        List<CarroResponseDTO> carros = carroService.byUserId(userId);
        if (carros.isEmpty()) {
            // Puedes decidir si devolver noContent() o un ok() con una lista vacía.
            // noContent() es común si consideras que la ausencia de carros es "nada que mostrar".
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(carros);
    }
}