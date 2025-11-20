package com.example.carro.service;

import com.example.carro.dto.CarroRequestDTO;
import com.example.carro.dto.CarroResponseDTO;
import com.example.carro.model.Carro;
import com.example.carro.repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public List<CarroResponseDTO> getAll() {
        return carroRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public CarroResponseDTO getCarroById(int id) {
        Carro carro = carroRepository.findById(id).orElse(null);
        if (carro == null) {
            return null;
        }
        return convertToResponseDTO(carro);
    }

    public CarroResponseDTO save(CarroRequestDTO carroDTO) {
        Carro carro = convertToEntity(carroDTO);
        Carro nuevoCarro = carroRepository.save(carro);
        return convertToResponseDTO(nuevoCarro);
    }

    public List<CarroResponseDTO> byUserId(int userId) {
        return carroRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos de conversión (mapeo) ---

    private CarroResponseDTO convertToResponseDTO(Carro carro) {
        return new CarroResponseDTO(carro.getId(), carro.getMarca(), carro.getModelo(), carro.getUserId());
    }

    private Carro convertToEntity(CarroRequestDTO carroDTO) {
        Carro carro = new Carro();
        carro.setMarca(carroDTO.getMarca());
        carro.setModelo(carroDTO.getModelo());
        carro.setUserId(carroDTO.getUserId());
        return carro;
    }
}