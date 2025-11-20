package com.example.carro.service;

import com.example.carro.dto.ProductoRequestDTO;
import com.example.carro.dto.ProductoResponseDTO;
import com.example.carro.model.Producto;
import com.example.carro.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoRequestDTO productoRequestDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto(1, "Manzana", 1.50, 100);
        productoRequestDTO = new ProductoRequestDTO("Manzana", 1.50, 100);
    }

    @Test
    void testGetAll() {
        // Given: El repositorio devolverá una lista con un producto
        when(productoRepository.findAll()).thenReturn(Collections.singletonList(producto));

        // When: Se llama al método getAll del servicio
        List<ProductoResponseDTO> result = productoService.getAll();

        // Then: El resultado no debe ser nulo y debe contener un elemento
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manzana", result.get(0).getNombre());
    }

    @Test
    void testGetProductoById_Found() {
        // Given: El repositorio encontrará un producto por su ID
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        // When: Se busca un producto por ID
        ProductoResponseDTO result = productoService.getProductoById(1);

        // Then: El resultado debe coincidir con los datos del producto
        assertNotNull(result);
        assertEquals("Manzana", result.getNombre());
    }

    @Test
    void testGetProductoById_NotFound() {
        // Given: El repositorio NO encontrará un producto por su ID
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        // When: Se busca un producto que no existe
        ProductoResponseDTO result = productoService.getProductoById(99);

        // Then: El resultado debe ser nulo
        assertNull(result);
    }

    @Test
    void testSave() {
        // Given: El repositorio guardará cualquier producto y lo devolverá con un ID
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        // When: Se guarda un nuevo producto
        ProductoResponseDTO result = productoService.save(productoRequestDTO);

        // Then: El resultado no debe ser nulo y debe tener el ID asignado
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Manzana", result.getNombre());
    }

    @Test
    void testUpdate_Success() {
        // Given
        ProductoRequestDTO updateRequest = new ProductoRequestDTO("Manzana Verde", 1.75, 80);
        Producto updatedProducto = new Producto(1, "Manzana Verde", 1.75, 80);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(updatedProducto);

        // When
        ProductoResponseDTO result = productoService.update(1, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals("Manzana Verde", result.getNombre());
        assertEquals(1.75, result.getPrecio());
    }

    @Test
    void testUpdate_NotFound() {
        // Given
        ProductoRequestDTO updateRequest = new ProductoRequestDTO("Manzana Verde", 1.75, 80);
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        // When
        ProductoResponseDTO result = productoService.update(99, updateRequest);

        // Then
        assertNull(result);
    }

    @Test
    void testDelete_Success() {
        // Given
        when(productoRepository.existsById(1)).thenReturn(true);

        // When
        boolean result = productoService.delete(1);

        // Then
        assertTrue(result);
        verify(productoRepository).deleteById(1); // Verifica que el método deleteById fue llamado
    }

    @Test
    void testDelete_NotFound() {
        // Given
        when(productoRepository.existsById(99)).thenReturn(false);

        // When
        boolean result = productoService.delete(99);

        // Then
        assertFalse(result);
    }
}