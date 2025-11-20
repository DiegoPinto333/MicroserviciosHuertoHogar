package com.example.carro.controller;
 
import com.example.carro.dto.ProductoRequestDTO;
import com.example.carro.dto.ProductoResponseDTO;
import com.example.carro.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/producto")
public class ProductoController {
 
    @Autowired
    private ProductoService productoService;
 
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        List<ProductoResponseDTO> productos = productoService.getAll();
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProducto(@PathVariable("id") int id) {
        ProductoResponseDTO producto = productoService.getProductoById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }
 
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> guardarProducto(@RequestBody ProductoRequestDTO productoDTO) {
        ProductoResponseDTO nuevoProducto = productoService.save(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }
}