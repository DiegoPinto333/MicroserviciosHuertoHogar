package com.example.carro.controller;

import com.example.carro.dto.ProductoRequestDTO;
import com.example.carro.dto.ProductoResponseDTO;
import com.example.carro.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductoResponseDTO productoResponseDTO;
    private ProductoRequestDTO productoRequestDTO;

    @BeforeEach
    void setUp() {
        productoResponseDTO = new ProductoResponseDTO(1, "Manzana", 1.50, 100);
        productoRequestDTO = new ProductoRequestDTO("Manzana", 1.50, 100);
    }

    @Test
    void testListarProductos() throws Exception {
        // Given
        when(productoService.getAll()).thenReturn(Collections.singletonList(productoResponseDTO));

        // When & Then
        mockMvc.perform(get("/producto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Manzana"));
    }

    @Test
    void testObtenerProducto_Found() throws Exception {
        // Given
        when(productoService.getProductoById(1)).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(get("/producto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Manzana"));
    }

    @Test
    void testObtenerProducto_NotFound() throws Exception {
        // Given
        when(productoService.getProductoById(anyInt())).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/producto/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGuardarProducto() throws Exception {
        // Given
        when(productoService.save(any(ProductoRequestDTO.class))).thenReturn(productoResponseDTO);

        // When & Then
        mockMvc.perform(post("/producto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testActualizarProducto_Success() throws Exception {
        // Given
        ProductoRequestDTO updateRequest = new ProductoRequestDTO("Manzana Verde", 1.75, 80);
        ProductoResponseDTO updatedResponse = new ProductoResponseDTO(1, "Manzana Verde", 1.75, 80);
        when(productoService.update(eq(1), any(ProductoRequestDTO.class))).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/producto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Manzana Verde"))
                .andExpect(jsonPath("$.precio").value(1.75));
    }

    @Test
    void testActualizarProducto_NotFound() throws Exception {
        // Given
        ProductoRequestDTO updateRequest = new ProductoRequestDTO("Manzana Verde", 1.75, 80);
        when(productoService.update(eq(99), any(ProductoRequestDTO.class))).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/producto/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProducto_Success() throws Exception {
        // Given
        when(productoService.delete(1)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/producto/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProducto_NotFound() throws Exception {
        // Given
        when(productoService.delete(99)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/producto/99"))
                .andExpect(status().isNotFound());
    }
}