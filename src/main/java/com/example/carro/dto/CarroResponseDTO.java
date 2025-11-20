package com.example.carro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarroResponseDTO {
    private int id;
    private String marca;
    private String modelo;
    private int userId;
}