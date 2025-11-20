package com.example.carro.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponseDTO {
    private int id;
    private String nombre;
    private double precio;
    private int stock;
}